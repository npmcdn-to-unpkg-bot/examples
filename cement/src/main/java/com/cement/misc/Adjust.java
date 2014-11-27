package com.cement.misc;

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
	
	public void calc(List<ReceiptMaterial> materials, int curveId, double totalQuantity) {
		List<CurveSieve> curve = jpa.loadCurveData(curveId);
		LeastSquaresAdjust lsa = new LeastSquaresAdjust(materials.size());
		Matrix coefs = new Matrix(materials.size(), 1);

		Map<Integer, Map<Integer, Double>> mmap = new HashMap<>();
		for (ReceiptMaterial rm : materials) {
			Material m = rm.getMaterial();
			Integer materialId = m.getId();
			Map<Integer, Double> mdata = jpa.loadMaterialSieveData(materialId);
			
			double sum = 0.0;
			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				Double Value = mdata.get(cs.getSieve());
				double v = Value == null ? 0 : Value;
				sum += v;
				mdata.put(cs.getSieve(), sum);
			}
			double D = 100 / sum;
			
			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				Double Value = mdata.get(cs.getSieve());
				double v = Value == null ? 0 : Value;
				v = v * D;
				mdata.put(cs.getSieve(), v);
			}
			
			mmap.put(materialId, mdata);
		}

		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			System.out.print("sieveId:" + MathUtil.l10(cs.getSieve()) + ", v:" + MathUtil.d4(cs.getValue()) + ", M:");
			for (ReceiptMaterial rm : materials) {
				Material m = rm.getMaterial();
				Integer materialId = m.getId();
				Map<Integer, Double> mdata = mmap.get(materialId);
				System.out.print(MathUtil.d4(mdata.get(cs.getSieve())) + " ");
			}
			System.out.println();
		}
		
		
		Matrix unknown = new Matrix(1, materials.size());
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
			unknown.setItem(0, rmIndex, v);
		}
		unknown.normalizePow2();
		
		System.out.println(unknown.sumPow2());
		unknown.printM("UNKNOWN");
		
		for (int i = 0; i < 5; i++) {
			lsa.clear();
			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				double L = -cs.getValue();
				for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
					ReceiptMaterial rm = materials.get(rmIndex);
					Integer materialId = rm.getMaterial().getId();
					Map<Integer, Double> mdata = mmap.get(materialId);
					Double v = mdata.get(cs.getSieve());
					if (v == null)
						throw new RuntimeException("null??? materialId=" + materialId);
					double k = unknown.getItem(0, rmIndex);
					double f = k * k * v;
					double dF_dk = 2 * k * v;

//					double f = k * v;
//					double dF_dk = v;
					L += f;
					coefs.setItem(rmIndex, 0, dF_dk);
				}
				lsa.addMeasurement(coefs, 1.0, L, 0);
			}

			double L = -1.0;
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				double k = unknown.getItem(0, rmIndex);
				double f = k * k;
				L += f;
				double dF_dk = 2 * k;
				coefs.setItem(rmIndex, 0, dF_dk);
			}
//			lsa.addMeasurement(coefs, 1.0, L, 0);

			if (!lsa.calculate())
				throw new RuntimeException();
			lsa.getUnknown().copyTo(unknown);
			double sum = unknown.sumPow2();
			System.out.println("SUM POW2 " + sum);
			//unknown.normalizePow2();
			
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				ReceiptMaterial rm = materials.get(rmIndex);
				rm.setQuantity(totalQuantity * unknown.getItem(0, rmIndex));
			}
		}
	}
}
