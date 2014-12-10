package com.cement.test.nnls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.cement.misc.NNLS;
import com.slavi.math.matrix.Matrix;

public class TestNNLS {
	void doIt() throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("TestNNLS_data.txt")));
		int unknownsCount = Integer.parseInt(r.readLine());
		int equationsCount = Integer.parseInt(r.readLine());
		r.readLine();
		Matrix A = new Matrix(unknownsCount, equationsCount);
		A.load(r);
		r.readLine();
		Matrix L = new Matrix(1, equationsCount);
		L.load(r);
		r.readLine();
		Matrix expected = new Matrix(1, unknownsCount);
		expected.load(r);
		r.close();
		
//		NonNegativeLeastSquares nnls = new NonNegativeLeastSquares(equationsCount, unknownsCount);
		NNLS nnls = new NNLS(equationsCount, unknownsCount);
		L.copyTo(nnls.b);
		for (int i = 0; i < equationsCount; i++) {
			for (int j = 0; j < unknownsCount; j++) {
				nnls.a.setItem(j, i, A.getItem(j, i));
			}
		}
		nnls.solve();
		
		for (int j = 0; j < unknownsCount; j++) {
			double d = nnls.x.getItem(0, j) - expected.getItem(0, j);
			if (Math.abs(d) > 0.0001) {
				System.out.println("Difference at index " + j);
				expected.printM("Expected");
				nnls.x.printM("Computed");
				throw new Error();
			}
		}
		
		System.out.println(Arrays.toString(nnls.index));
	}

	public static void main(String[] args) throws Exception {
		new TestNNLS().doIt();
		System.out.println("Done.");
	}
}
