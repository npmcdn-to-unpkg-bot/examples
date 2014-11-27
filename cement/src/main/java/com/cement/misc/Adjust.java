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
	
	public static double sumPow2(Matrix m) {
		double D = 0;
		for (int i = m.getSizeX() - 1; i >= 0; i--)
			for (int j = m.getSizeY() - 1; j >= 0; j--) {
				double v = m.getItem(i, j);
				D += v*v;
			}
		return D;
	}
	
	public static boolean normalizePow2(Matrix m) {
		double D = sumPow2(m);
		if (D == 0) {
			m.make0();
			return false;
		}
		D = 1 / Math.sqrt(D);
		m.rMul(D);
		return true;
	}
	
	public void calc(List<ReceiptMaterial> materials, int curveId, double totalQuantity) {
		List<CurveSieve> curve = jpa.loadCurveData(curveId);
		LeastSquaresAdjust lsa = new LeastSquaresAdjust(materials.size());
		Matrix coefs = new Matrix(materials.size(), 1);
/*
		System.out.println("Curve ID:" + curveId);
		for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
			CurveSieve cs = curve.get(curveIndex);
			System.out.println("sieveId:" + cs.getSieve() + ", v:" + MathUtil.d4(cs.getValue()));
		}
*/
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
			
//			System.out.println("Material ID:" + materialId);
			for (int curveIndex = 0; curveIndex < curve.size(); curveIndex++) {
				CurveSieve cs = curve.get(curveIndex);
				Double Value = mdata.get(cs.getSieve());
				double v = Value == null ? 0 : Value;
				v = v * D;
				mdata.put(cs.getSieve(), v);
//				System.out.println("sieveId:" + cs.getSieve() + ", v:" + MathUtil.d4(v));
			}
			
			mmap.put(materialId, mdata);
		}

		Matrix unknown = new Matrix(materials.size(), 1);
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
			unknown.setItem(rmIndex, 0, v);
		}
		//normalizePow2(unknown);
		unknown.normalize();
		
		System.out.println(sumPow2(unknown));
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
					double k = unknown.getItem(rmIndex, 0);
//					double f = k * k * v;
//					double dF_dk = 2 * k * v;

					double f = k * v;
					double dF_dk = v;
					L += f;
					coefs.setItem(rmIndex, 0, dF_dk);
				}
				lsa.addMeasurement(coefs, 1.0, L, 0);
			}

			double L = -1.0;
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				double k = unknown.getItem(rmIndex, 0);
				double f = k * k;
				L += f;
				double dF_dk = 2 * k;
				coefs.setItem(rmIndex, 0, dF_dk);
			}
//			lsa.addMeasurement(coefs, 1.0, L, 0);

			if (!lsa.calculate())
				throw new RuntimeException();
			unknown = lsa.getUnknown();
			double sum = sumPow2(unknown);
			System.out.println("SUM POW2 " + sum);
			//unknown.normalize();
			
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				ReceiptMaterial rm = materials.get(rmIndex);
				rm.setQuantity(totalQuantity * unknown.getItem(rmIndex, 0));
			}
		}
	}
}
