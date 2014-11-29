package com.cement.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cement.component.ReceiptSetJpa;
import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;
import com.slavi.math.MathUtil;
import com.slavi.math.adjust.LeastSquaresAdjust;
import com.slavi.math.matrix.Matrix;

public class Adjust {

	ReceiptSetJpa jpa;

	public Adjust(ReceiptSetJpa jpa) {
		this.jpa = jpa;
	}
	
	boolean pow2 = false;
	
	public void processMaterialData(List<CurveSieve> curve, Map<Integer, Double> mdata) {
		double sum = 0.0;
		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			Double Value = mdata.get(cs.getSieve());
			double v = Value == null ? 0 : Value;
			sum += v;
			mdata.put(cs.getSieve(), sum);
		}
		double D = 1 / sum;
		
		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			Double Value = mdata.get(cs.getSieve());
			double v = Value == null ? 0 : Value;
			v = v * D;
			mdata.put(cs.getSieve(), v);
		}
	}
	
	public List<Double> calc(List<CurveSieve> curve, List<ReceiptMaterial> materials, int curveId, double totalQuantity) {
		LeastSquaresAdjust lsa = new LeastSquaresAdjust(materials.size());
		Matrix coefs = new Matrix(materials.size(), 1);

		Map<Integer, Map<Integer, Double>> mmap = new HashMap<>();
		for (ReceiptMaterial rm : materials) {
			Material m = rm.getMaterial();
			Integer materialId = m.getId();
			Map<Integer, Double> mdata = jpa.loadMaterialSieveData(materialId);
			processMaterialData(curve, mdata);
			mmap.put(materialId, mdata);
		}

		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			System.out.print("sieveId:" + MathUtil.l10(cs.getSieve()) + ", v:" + MathUtil.d4(cs.getValue() / 100) + ", M:");
			for (ReceiptMaterial rm : materials) {
				Material m = rm.getMaterial();
				Integer materialId = m.getId();
				Map<Integer, Double> mdata = mmap.get(materialId);
				System.out.print(MathUtil.d4(mdata.get(cs.getSieve())) + " ");
			}
			System.out.println();
		}
		
		
		Matrix K = new Matrix(1, materials.size());
		for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
			ReceiptMaterial rm = materials.get(rmIndex);
			if (rm.getMaxQuantity() > totalQuantity)
				rm.setMaxQuantity(totalQuantity);
			if (rm.getMaxQuantity() < 0)
				rm.setMaxQuantity(totalQuantity);

			if (rm.getMinQuantity() > rm.getMaxQuantity())
				rm.setMinQuantity(0);
			if (rm.getMinQuantity() < 0)
				rm.setMinQuantity(0);
			
			double v = rm.getMinQuantity();
			v = Math.min(v, rm.getMaxQuantity());
			v = Math.max(v, rm.getMinQuantity());
			if (v == 0.0)
				v = 1.0;
			K.setItem(0, rmIndex, v);
		}

		if (pow2) 
			K.normalizePow2();
		else
			K.normalize();
		K.termAbs(K);

		for (int i = 0; i < 5; i++) {
			K.printM("K");
			System.out.println("SUM K: " + (pow2 ? K.sumPow2() : K.sumAll()));
			
			lsa.clear();
			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				double L = -cs.getValue() / 100;
				for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
					ReceiptMaterial rm = materials.get(rmIndex);
					Integer materialId = rm.getMaterial().getId();
					Map<Integer, Double> mdata = mmap.get(materialId);
					Double v = mdata.get(cs.getSieve());
					if (v == null)
						throw new RuntimeException("null??? materialId=" + materialId);
					double k = K.getItem(0, rmIndex);
					
					double f;
					double dF_dk;
					
					if (pow2) {
						f = k * k * v;
						dF_dk = 2 * k * v;
					} else {
						f = k * v;
						dF_dk = v;
					}
					
					L += f;
					coefs.setItem(rmIndex, 0, dF_dk);
				}
				lsa.addMeasurement(coefs, 1.0, -L, 0);
			}

			double L = -1.0;
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				double k = K.getItem(0, rmIndex);
				double f;
				double dF_dk;

				if (pow2) {
					f = k * k;
					dF_dk = 2 * k;
				} else {
					f = k;
					dF_dk = 1;
				}
				
				L += f;
				coefs.setItem(rmIndex, 0, dF_dk);
			}
			lsa.addMeasurement(coefs, 1.0, -L, 0);

			if (!lsa.calculate())
				throw new RuntimeException();
			Matrix unknown = lsa.getUnknown();
			K.mSum(unknown, K);
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				ReceiptMaterial rm = materials.get(rmIndex);
				rm.setQuantity(totalQuantity * K.getItem(0, rmIndex));
			}
		}

		K.termAbs(K);
		if (pow2) 
			K.normalizePow2();
		else
			K.normalize();

		K.printM("Final K");
		System.out.println("SUM K: " + (pow2 ? K.sumPow2() : K.sumAll()));
		
		List<Double> r = new ArrayList<>();
		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			
			double curV = 0;
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				ReceiptMaterial rm = materials.get(rmIndex);
				Integer materialId = rm.getMaterial().getId();
				Map<Integer, Double> mdata = mmap.get(materialId);
				
				double v = mdata.get(cs.getSieve());
				double k = K.getItem(0, rmIndex);
				if (pow2) {
					curV += k * k * v;
				} else {
					curV += k * v;
				}
			}
			r.add(curV * 100);
		}
		System.out.println("Result material:");
		System.out.println(r);
		return r;
	}
}
