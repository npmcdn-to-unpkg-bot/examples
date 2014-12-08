package com.cement.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cement.NNLS;
import com.cement.component.ReceiptSetJpa;
import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;
import com.slavi.math.MathUtil;
import com.slavi.math.adjust.LeastSquaresAdjust;
import com.slavi.math.matrix.Matrix;
import com.slavi.math.matrix.SymmetricMatrix;

public class Adjust {

	ReceiptSetJpa jpa;

	public Adjust(ReceiptSetJpa jpa) {
		this.jpa = jpa;
	}
	
	boolean pow2 = false;

	public void processMaterialData(List<CurveSieve> curve, Map<Integer, Double> mdata) {
		double sum = 0.0;
		for (CurveSieve cs : curve) {
			Double Value = mdata.get(cs.getSieve());
			double v = Value == null ? 0 : Value;
			sum += v;
			mdata.put(cs.getSieve(), sum);
		}

		if (sum == 0)
			throw new Error();
		double D = 1 / sum;
		
		for (CurveSieve cs : curve) {
			Double Value = mdata.get(cs.getSieve());
			double v = Value == null ? 0 : Value;
			v = v * D;
			mdata.put(cs.getSieve(), v);
		}
	}
	
	public List<Double> calc(List<CurveSieve> curve, List<ReceiptMaterial> materials, int curveId, double totalQuantity) {
		if (totalQuantity <= 0)
			throw new Error();
		
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

		Matrix curve_mat = new Matrix(3, curve.size());
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
			curve_mat.setItem(0, curveIndex, cs.getLowValue());
			curve_mat.setItem(1, curveIndex, cs.getUpperValue());
			curve_mat.setItem(2, curveIndex, cs.getValue());
		}
		System.out.println(curve_mat.toMatlabString("C"));
		
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
			
			double v = rm.getQuantity();
			v = Math.min(v, rm.getMaxQuantity());
			v = Math.max(v, rm.getMinQuantity());
			if (v == 0.0)
				v = 1.0;
			else
				v /= totalQuantity;
			K.setItem(0, rmIndex, v);
		}
		System.out.println(K.toMatlabString("X"));
		
		Matrix A = new Matrix(materials.size(), curve.size());
		NNLS nnls = new NNLS(curve.size(), materials.size());
		for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
			ReceiptMaterial rm = materials.get(rmIndex);
			Integer materialId = rm.getMaterial().getId();
			Map<Integer, Double> mdata = mmap.get(materialId);

			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				Double v = mdata.get(cs.getSieve());
				A.setItem(rmIndex, curveIndex, v);
				nnls.a[curveIndex][rmIndex] = v;
			}
		}
		Matrix R = new Matrix(1, curve.size());
		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			R.setItem(0, curveIndex, cs.getValue());
			nnls.b[curveIndex] = cs.getValue();
		}
		System.out.println(A.toMatlabString("A"));
		System.out.println(R.toMatlabString("L"));

		nnls.solve();
		Matrix nnlsX = new Matrix(1, materials.size());
		for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) {
			nnlsX.setItem(0, rmIndex, nnls.x[rmIndex]);
		}
		System.out.println(nnlsX.toMatlabString("NNLS_X"));
		
		//////////////////////////////////////////////////////////
		
		

		
		//////////////////////////////////////////////////////////
		
		for (int i = 0; ; i++) {
			K.printM("K");
			System.out.println("SUM K: " + (pow2 ? K.sumPow2() : K.sumAll()));

			K.termAbs(K);
			if (pow2) 
				K.normalizePow2();
			else
				K.normalize();

			K.printM("K - normalized");
			System.out.println("SUM K normalized: " + (pow2 ? K.sumPow2() : K.sumAll()));

			lsa.clear();
			for (CurveSieve cs : curve) {
				double L = -cs.getValue() / 100;
				for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
					ReceiptMaterial rm = materials.get(rmIndex);
					Integer materialId = rm.getMaterial().getId();
					Map<Integer, Double> mdata = mmap.get(materialId);
					Double v = mdata.get(cs.getSieve());
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
/*
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
*/
			SymmetricMatrix nmCopy = lsa.getNm().makeCopy();
			double det = nmCopy.makeSquareMatrix().det();
			System.out.println("DET(NM) = " + MathUtil.d4(det));
			
			if (!lsa.calculate())
				throw new RuntimeException();

			if (i >= 3)
				break;
			
			Matrix unknown = lsa.getUnknown();
			K.mSum(unknown, K);
		}

		for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
			ReceiptMaterial rm = materials.get(rmIndex);
			double k = K.getItem(0, rmIndex);
			if (pow2)
				k = k * k;
			rm.setQuantity(totalQuantity * k);
		}
		
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
