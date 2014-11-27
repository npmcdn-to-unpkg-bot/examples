package com.cement.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import com.cement.component.ReceiptSetJpa;
import com.cement.model.CurveSieve;
import com.cement.model.ReceiptMaterial;
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
			Integer materialId = rm.getMaterial().getId();
			Map<Integer, Double> mdata = jpa.loadMaterialSieveData(materialId);
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
		unknown.normalize();
		
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
					double f = k * v;
					L += f;
					double dF_dk = v;
					coefs.setItem(rmIndex, 0, dF_dk);
				}
				lsa.addMeasurement(coefs, 1.0, L, 0);
			}
			if (!lsa.calculate())
				throw new RuntimeException();
			unknown = lsa.getUnknown();
			double sum = unknown.sumAll();
			System.out.println("SUM UNKNOWN " + sum);
			unknown.normalize();
			
			for (int rmIndex = 0; rmIndex < materials.size(); rmIndex++) { 
				ReceiptMaterial rm = materials.get(rmIndex);
				rm.setQuantity(totalQuantity * unknown.getItem(rmIndex, 0));
			}
		}
	}
}
