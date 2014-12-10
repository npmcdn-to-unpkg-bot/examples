package com.cement.misc;

import com.slavi.math.MathUtil;
import com.slavi.math.matrix.Matrix;

/**
 * Class NonNegativeLeastSquares provides a method for solving a least squares minimization problem with nonnegativity
 * constraints. The <TT>solve()</TT> method finds an approximate solution to the linear system of equations <B>Ax</B> =
 * <B>b</B>, such that ||<B>Ax</B>&nbsp;-&nbsp;<B>b</B>||<SUP>2</SUP> is minimized, and such that <B>x</B> &gt;=
 * <B>0</B>. The inputs to and outputs from the <TT>solve()</TT> method are stored in the fields of an instance of class
 * NonNegativeLeastSquares.
 * <P>
 * The Java code is a translation of the Fortran subroutine <TT>NNLS</TT> from Charles L. Lawson and Richard J. Hanson,
 * <I>Solving Least Squares Problems</I> (Society for Industrial and Applied Mathematics, 1995), page 161.
 *
 * @author Alan Kaminsky
 * @version 22-Apr-2005
 */
public class NNLS {

	// Exported data members.

	/**
	 * The number of rows, typically the number of input data points, in the least squares problem.
	 */
	public final int measurementCount;

	/**
	 * The number of columns, typically the number of output parameters, in the least squares problem.
	 */
	public final int numUnknown;

	/**
	 * The <I>M</I>x<I>N</I>-element <B>A</B> matrix for the least squares problem. On input to the <TT>solve()</TT>
	 * method, <TT>a</TT> contains the matrix <B>A</B>. On output, <TT>a</TT> has been replaced with <B>QA</B>, where
	 * <B>Q</B> is an <I>M</I>x<I>M</I>-element orthogonal matrix generated during the <TT>solve()</TT> method's
	 * execution.
	 */
	public final Matrix a;

	/**
	 * The <I>M</I>-element <B>b</B> vector for the least squares problem. On input to the <TT>solve()</TT> method,
	 * <TT>b</TT> contains the vector <B>b</B>. On output, <TT>b</TT> has been replaced with <B>Qb</B>, where <B>Q</B>
	 * is an <I>M</I>x<I>M</I>-element orthogonal matrix generated during the <TT>solve()</TT> method's execution.
	 */
	public final Matrix b;

	/**
	 * The <I>N</I>-element <B>x</B> vector for the least squares problem. On output from the <TT>solve()</TT> method,
	 * <TT>x</TT> contains the solution vector <B>x</B>.
	 */
	public final Matrix x;

	/**
	 * The <I>N</I>-element index vector. On output from the <TT>solve()</TT> method: <TT>index[0]</TT> through
	 * <TT>index[nsetp-1]</TT> contain the indexes of the elements in <B>x</B> that are in set <I>P,</I> the set of
	 * positive values; that is, the elements that are not forced to be zero (inactive constraints).
	 * <TT>index[nsetp]</TT> through <TT>index[N-1]</TT> contain the indexes of the elements in <B>x</B> that are in set
	 * <I>Z,</I> the set of zero values; that is, the elements that are forced to be zero (active constraints).
	 */
	public final int[] index;

	/**
	 * The number of elements in the set <I>P;</I> that is, the number of positive values (inactive constraints). An
	 * output of the <TT>solve()</TT> method.
	 */
	public int nsetp;

	/**
	 * The squared Euclidean norm of the residual vector, ||<B>Ax</B> - <B>b</B>||<SUP>2</SUP>. An output of the
	 * <TT>solve()</TT> method.
	 */
	public double normsqr;

	// Working storage.
	private final double[] w;
	private final Matrix zz;
	private final double[] terms;

	// Maximum number of iterations.
	private final int itmax;

	// Magic numbers.
	private static final double factor = 0.01;

	// Exported constructors.

	/**
	 * Construct a new nonnegative least squares problem of the given size. Fields <TT>M</TT> and <TT>N</TT> are set to
	 * the given values. The array fields <TT>a</TT>, <TT>b</TT>, <TT>x</TT>, and <TT>index</TT> are allocated with the
	 * proper sizes but are not filled in.
	 *
	 * @param M
	 *            Number of rows (input data points) in the least squares problem.
	 * @param N
	 *            Number of columns (output parameters) in the least squares problem.
	 *
	 * @exception IllegalArgumentException
	 *                (unchecked exception) Thrown if <TT>M</TT> &lt;= 0 or <TT>N</TT> &lt;= 0.
	 */
	public NNLS(int measurementCount, int numUnknown) {
		if (measurementCount <= 0) {
			throw new IllegalArgumentException("NonNegativeLeastSquares(): M = " + measurementCount + " illegal");
		}
		if (numUnknown <= 0) {
			throw new IllegalArgumentException("NonNegativeLeastSquares(): N = " + numUnknown + " illegal");
		}

		this.measurementCount = measurementCount;
		this.numUnknown = numUnknown;
		this.a = new Matrix(measurementCount, numUnknown);
		this.b = new Matrix(1, measurementCount);
		this.x = new Matrix(1, numUnknown);
		this.index = new int[numUnknown];

		this.w = new double[numUnknown];
		this.zz = new Matrix(1, measurementCount);
		this.terms = new double[2];
		this.itmax = 3 * numUnknown;
	}

	// Exported operations.

	/**
	 * Solve this least squares minimization problem with nonnegativity constraints. The <TT>solve()</TT> method finds
	 * an approximate solution to the linear system of equations <B>Ax</B> = <B>b</B>, such that
	 * ||<B>Ax</B>&nbsp;-&nbsp;<B>b</B>||<SUP>2</SUP> is minimized, and such that <B>x</B> &gt;= <B>0</B>. On input, the
	 * field <TT>a</TT> must be filled in with the matrix <B>A</B> and the field <TT>b</TT> must be filled in with the
	 * vector <B>b</B> for the problem to be solved. On output, the other fields are filled in with the solution as
	 * explained in the documentation for each field.
	 *
	 * @exception TooManyIterationsException
	 *                (unchecked exception) Thrown if too many iterations occurred without finding a minimum (more than
	 *                3<I>N</I> iterations).
	 */
	public void solve() {
		// Keep count of iterations.
		int iter = 0;

		// Initialize the arrays index and x.
		// index[0] through index[nsetp-1] = set P.
		// index[nsetp] through index[N-1] = set Z.
		x.make0();
		for (int i = 0; i < numUnknown; ++i) {
			index[i] = i;
		}
		nsetp = 0;

		// Main loop begins here.
		mainloop: for (;;) {
			// Quit if all coefficients are already in the solution, or if M
			// columns of A have been triangularized.
			if (nsetp >= numUnknown || nsetp >= measurementCount)
				break;

			// Compute components of the dual (negative gradient) vector W.
			for (int i = nsetp; i < numUnknown; ++i) {
				int indexI = index[i];
				double sm = 0.0;
				for (int l = nsetp; l < measurementCount; ++l) {
					sm += a.getItem(l, indexI) * b.getItem(0, l);
				}
				w[indexI] = sm;
			}

			// Find a candidate j to be moved from set Z to set P.
			int indexIZ = -1;
			int izmaxLast = -1;
			double up = 0.0;
			while (true) {
				// Find largest positive W[j].
				double wmax = 0.0;
				int izmax = -1;
				for (int i = nsetp; i < numUnknown; ++i) {
					int indexI = index[i];
					if (w[indexI] > wmax) {
						wmax = w[indexI];
						izmax = i;
					}
				}

				// If wmax <= 0, terminate. This indicates satisfaction of the
				// Kuhn-Tucker conditions.
				if (wmax <= 0.0)
					break mainloop;
				izmaxLast = izmax;
				indexIZ = index[izmaxLast];

				// The sign of W[j] is okay for j to be moved to set P. Begin
				// the transformation and check new diagonal element to avoid
				// near linear independence.
				double asave = a.getItem(nsetp, indexIZ);
				up = constructHouseholderTransform(nsetp, nsetp + 1, a, indexIZ);
				double unorm = 0.0;
				for (int l = 0; l < nsetp; ++l) {
					unorm += a.getItem(l, indexIZ) * a.getItem(l, indexIZ);
				}
				unorm = Math.sqrt(unorm);
				if (unorm + Math.abs(a.getItem(nsetp, indexIZ) * factor) - unorm > 0.0) {
					// Column j is sufficiently independent. Copy B into ZZ,
					// update ZZ, and solve for ztest = proposed new value for
					// X[j].
					b.copyTo(zz);
					applyHouseholderTransform(nsetp, nsetp + 1, a, indexIZ, up, zz);
					double ztest = zz.getItem(0, nsetp) / a.getItem(nsetp, indexIZ);

					// If ztest is positive, we've found our candidate.
					if (ztest > 0.0)
						break;
				}

				// Reject j as a candidate to be moved from set Z to set P.
				// Restore a[nsetp][j], set w[j] = 0, and try again.
				a.setItem(nsetp, indexIZ, asave);
				w[indexIZ] = 0.0;
			}

			// The index j = index[iz] has been selected to be moved from set Z
			// to set P. Update B, update indexes, apply Householder
			// transformations to columns in new set Z, zero subdiagonal
			// elements in column j, set w[j] = 0.
			zz.copyTo(b);

			index[izmaxLast] = index[nsetp];
			index[nsetp] = indexIZ;
			++nsetp;

			int indexJZ = -1;
			for (int jz = nsetp; jz < numUnknown; ++jz) {
				indexJZ = index[jz];
				applyHouseholderTransform(nsetp - 1, nsetp, a, indexIZ, up, a, indexJZ);
			}

			for (int l = nsetp; l < measurementCount; ++l) {
				a.setItem(l, indexIZ, 0.0);
			}

			w[indexIZ] = 0.0;

			// Solve the triangular system. Store the solution temporarily in
			// zz.
			for (int l = 0; l < nsetp; ++l) {
				int ip = nsetp - l;
				if (l != 0) {
					for (int i = 0; i < ip; ++i) {
						zz.setItem(0, i, zz.getItem(0, i) - a.getItem(i, indexJZ) * zz.getItem(0, ip));
					}
				}
				--ip;
				indexJZ = index[ip];
				zz.setItem(0, ip, zz.getItem(0, ip) / a.getItem(ip, indexJZ));
			}

			// Secondary loop begins here.
			for (;;) {
				// Increment iteration counter.
				++iter;
				if (iter > itmax) {
					throw new Error("NonNegativeLeastSquares.solve(): Too many iterations");
				}

				// See if all new constrained coefficients are feasible. If not,
				// compute alpha.
				double alpha = 2.0;
				for (int i = 0; i < nsetp; ++i) {
					int indexI = index[i];
					if (zz.getItem(0, i) <= 0.0) {
						double tmpX = x.getItem(0, indexI);
						double t = -tmpX / (zz.getItem(0, i) - tmpX);
						if (alpha > t) {
							alpha = t;
							indexJZ = i;
						}
					}
				}

				// If all new constrained coefficients are feasible then alpha
				// will still be 2. If so, exit from secondary loop to main
				// loop.
				if (alpha == 2.0)
					break;

				// Otherwise, use alpha (which will be between 0 and 1) to
				// interpolate between the old x and the new zz.
				for (int i = 0; i < nsetp; ++i) {
					int indexI = index[i];
					double tmpX = x.getItem(0, indexI);
					x.setItem(0, indexI, tmpX + alpha * (zz.getItem(0, i) - tmpX));
				}

				// Modify A and B and the index arrays to move coefficient i
				// from set P to set Z.
				int indexJJ = index[indexJZ];
				tertiaryloop: for (;;) {
					x.setItem(0, indexJJ, 0.0);
					if (indexJZ != nsetp - 1) {
						++indexJZ;
						for (int j = indexJZ; j < nsetp; ++j) {
							int indexJ = index[j];
							index[j - 1] = indexJ;
							a.setItem(j - 1, indexJ, computeGivensRotation(a.getItem(j - 1, indexJ), a.getItem(j, indexJ), terms));
							a.setItem(j, indexJ, 0.0);
							double cc = terms[0];
							double ss = terms[1];
							for (int l = 0; l < numUnknown; ++l) {
								if (l != indexJ) {
									// Apply Givens rotation to column l of A.
									double temp = a.getItem(j - 1, l);
									a.setItem(j - 1, l, cc * temp + ss * a.getItem(j, l));
									a.setItem(j, l, -ss * temp + cc * a.getItem(j, l));
								}
							}
							// Apply Givens rotation to B.
							double temp = b.getItem(0, j - 1);
							b.setItem(0, j - 1, cc * temp + ss * b.getItem(0, j));
							b.setItem(0, j, -ss * temp + cc * b.getItem(0, j));
						}
					}
					--nsetp;
					index[nsetp] = indexJJ;

					// See if the remaining coefficients in set P are feasible.
					// They should be because of the way alpha was determined.
					// If any are infeasible it is due to roundoff error. Any
					// that are nonpositive will be set to 0 and moved from set
					// P to set Z.
					for (indexJZ = 0; indexJZ < nsetp; ++indexJZ) {
						indexJJ = index[indexJZ];
						if (x.getItem(0, indexJJ) <= 0.0)
							continue tertiaryloop;
					}
					break;
				}

				// Copy b into zz, then solve the tridiagonal system again and
				// continue the secondary loop.
				b.copyTo(zz);
				for (int l = 0; l < nsetp; ++l) {
					int ip = nsetp - l;
					if (l != 0) {
						for (int i = 0; i < ip; ++i) {
							zz.setItem(0, i, zz.getItem(0, i) - a.getItem(i, indexJZ) * zz.getItem(0, ip));
						}
					}
					--ip;
					indexJZ = index[ip];
					zz.setItem(0, ip, zz.getItem(0, ip) / a.getItem(ip, indexJZ));
				}
			}

			// Update x from zz.
			for (int i = 0; i < nsetp; ++i) {
				int indexI = index[i];
				x.setItem(0, indexI, zz.getItem(0, i));
			}

			// All new coefficients are positive. Continue the main loop.
		}

		// Compute the squared Euclidean norm of the final residual vector.
		normsqr = 0.0;
		for (int i = nsetp; i < measurementCount; ++i) {
			double tmpB = b.getItem(0, i);
			normsqr += tmpB * tmpB;
		}
	}

	// Hidden operations.

	/**
	 * Construct a Householder transformation. <TT>u</TT> is an <I>M</I>x<I>N</I>-element matrix used as an input and an
	 * output of this method.
	 *
	 * @param ipivot
	 *            Index of the pivot element within the pivot vector.
	 * @param i1
	 *            If <TT>i1</TT> &lt; <I>M,</I> the transformation will be constructed to zero elements indexed from
	 *            <TT>i1</TT> through <I>M</I>-1. If <TT>i1</TT> &gt;= <I>M,</I> an identity transformation will be
	 *            constructed.
	 * @param u
	 *            An <I>M</I>x<I>N</I>-element matrix. On input, column <TT>pivotcol</TT> of <TT>u</TT> contains the
	 *            pivot vector. On output, column <TT>pivotcol</TT> of <TT>u</TT>, along with the return value (
	 *            <TT>up</TT>), contains the Householder transformation.
	 * @param pivotcol
	 *            Index of the column of <TT>u</TT> that contains the pivot vector.
	 *
	 * @return The quantity <TT>up</TT> which is part of the Householder transformation.
	 */
	private static double constructHouseholderTransform(int ipivot, int i1, Matrix u, int pivotcol) {
		int M = u.getSizeX();
		double cl = Math.abs(u.getItem(ipivot, pivotcol));

		// Construct the transformation.
		for (int j = i1; j < M; ++j) {
			cl = Math.max(Math.abs(u.getItem(j, pivotcol)), cl);
		}
		if (cl <= 0.0) {
			throw new IllegalArgumentException(
					"NonNegativeLeastSquares.constructHouseholderTransform(): Illegal pivot vector");
		}
		double clinv = 1.0 / cl;
		double tmp = u.getItem(ipivot, pivotcol) * clinv;
		double sm = tmp * tmp;
		for (int j = i1; j < M; ++j) {
			tmp = u.getItem(j, pivotcol) * clinv;
			sm += tmp * tmp;
		}
		cl = cl * Math.sqrt(sm);
		if (u.getItem(ipivot, pivotcol) > 0.0)
			cl = -cl;
		double up = u.getItem(ipivot, pivotcol) - cl;
		u.setItem(ipivot, pivotcol, cl);
		return up;
	}

	/**
	 * Apply a Householder transformation to one column of a matrix. <TT>u</TT> is an <I>M</I>x<I>N</I>-element matrix
	 * used as an input of this method. <TT>c</TT> is an <I>M</I>x<I>N</I>-element matrix used as an input and output of
	 * this method. <TT>ipivot</TT>, <TT>i1</TT>, <TT>u</TT>, and <TT>pivotcol</TT> must be the same as in a previous
	 * call of <TT>constructHouseholderTransform()</TT>, and <TT>up</TT> must be the value returned by that method call.
	 *
	 * @param ipivot
	 *            Index of the pivot element within the pivot vector.
	 * @param i1
	 *            If <TT>i1</TT> &lt; <I>M,</I> the transformation will zero elements indexed from <TT>i1</TT> through
	 *            <I>M</I>-1. If <TT>i1</TT> &gt;= <I>M,</I> the transformation is an identity transformation.
	 * @param u
	 *            An <I>M</I>x<I>N</I>-element matrix. On input, column <TT>pivotcol</TT> of <TT>u</TT>, along with
	 *            <TT>up</TT>, contains the Householder transformation. This must be the output of a previous call of
	 *            <TT>constructHouseholderTransform()</TT>.
	 * @param pivotcol
	 *            Index of the column of <TT>u</TT> that contains the Householder transformation.
	 * @param up
	 *            The rest of the Householder transformation. This must be the return value of the same previous call of
	 *            <TT>constructHouseholderTransform()</TT>.
	 * @param c
	 *            An <I>M</I>x<I>N</I>-element matrix. On input, column <TT>applycol</TT> of <TT>c</TT> contains the
	 *            vector to which the Householder transformation is to be applied. On output, column <TT>applycol</TT>
	 *            of <TT>c</TT> contains the transformed vector.
	 * @param applycol
	 *            Index of the column of <TT>c</TT> to which the Householder transformation is to be applied.
	 */
	private static void applyHouseholderTransform(int ipivot, int i1, Matrix u, int pivotcol, double up,
			Matrix c, int applycol) {
		int M = u.getSizeX();
		double cl = Math.abs(u.getItem(ipivot, pivotcol));
		if (cl <= 0.0) {
			throw new IllegalArgumentException(
					"NonNegativeLeastSquares.applyHouseholderTransform(): Illegal pivot vector");
		}

		double b = up * u.getItem(ipivot, pivotcol);
		// b must be nonpositive here. If b = 0, return.
		if (b == 0.0) {
			return;
		} else if (b > 0.0) {
			throw new IllegalArgumentException(
					"NonNegativeLeastSquares.applyHouseholderTransform(): Illegal pivot element");
		}
		b = 1.0 / b;

		double sm = c.getItem(ipivot, applycol) * up;
		for (int i = i1; i < M; ++i) {
			sm += c.getItem(i, applycol) * u.getItem(i, pivotcol);
		}
		if (sm != 0.0) {
			sm = sm * b;
			c.setItem(ipivot, applycol, c.getItem(ipivot, applycol) + sm * up);
			for (int i = i1; i < M; ++i) {
				c.setItem(i, applycol, c.getItem(i, applycol) + sm * u.getItem(i, pivotcol));
			}
		}
	}

	/**
	 * Apply a Householder transformation to a vector. <TT>u</TT> is an <I>M</I>x<I>N</I>-element matrix used as an
	 * input of this method. <TT>c</TT> is an <I>M</I>-element array used as an input and output of this method.
	 * <TT>ipivot</TT>, <TT>i1</TT>, <TT>u</TT>, and <TT>pivotcol</TT> must be the same as in a previous call of
	 * <TT>constructHouseholderTransform()</TT>, and <TT>up</TT> must be the value returned by that method call.
	 *
	 * @param ipivot
	 *            Index of the pivot element within the pivot vector.
	 * @param i1
	 *            If <TT>i1</TT> &lt; <I>M,</I> the transformation will zero elements indexed from <TT>i1</TT> through
	 *            <I>M</I>-1. If <TT>i1</TT> &gt;= <I>M,</I> the transformation is an identity transformation.
	 * @param u
	 *            An <I>M</I>x<I>N</I>-element matrix. On input, column <TT>pivotcol</TT> of <TT>u</TT>, along with
	 *            <TT>up</TT>, contains the Householder transformation. This must be the output of a previous call of
	 *            <TT>constructHouseholderTransform()</TT>.
	 * @param pivotcol
	 *            Index of the column of <TT>u</TT> that contains the Householder transformation.
	 * @param up
	 *            The rest of the Householder transformation. This must be the return value of the same previous call of
	 *            <TT>constructHouseholderTransform()</TT>.
	 * @param c
	 *            An <I>M</I>-element array. On input, <TT>c</TT> contains the vector to which the Householder
	 *            transformation is to be applied. On output, <TT>c</TT> contains the transformed vector.
	 */
	private static void applyHouseholderTransform(int ipivot, int i1, Matrix u, int pivotcol, double up, Matrix c) {
		int M = u.getSizeX();
		double cl = Math.abs(u.getItem(ipivot, pivotcol));
		if (cl <= 0.0) {
			throw new IllegalArgumentException(
					"NonNegativeLeastSquares.applyHouseholderTransform(): Illegal pivot vector");
		}

		double b = up * u.getItem(ipivot, pivotcol);
		// b must be nonpositive here. If b = 0, return.
		if (b == 0.0) {
			return;
		} else if (b > 0.0) {
			throw new IllegalArgumentException(
					"NonNegativeLeastSquares.applyHouseholderTransform(): Illegal pivot element");
		}
		b = 1.0 / b;

		double sm = c.getItem(0, ipivot) * up;
		for (int i = i1; i < M; ++i) {
			sm += c.getItem(0, i) * u.getItem(i, pivotcol);
		}
		if (sm != 0.0) {
			sm = sm * b;
			c.setItem(0, ipivot, c.getItem(0, ipivot) + sm * up);
			for (int i = i1; i < M; ++i) {
				c.setItem(0, i, c.getItem(0, i) + sm * u.getItem(i, pivotcol));
			}
		}
	}

	/**
	 * Compute the sine and cosine terms of a Givens rotation matrix. The terms <TT>c</TT> and <TT>s</TT> are returned
	 * in <TT>terms[0]</TT> and <TT>terms[1]</TT>, respectively, such that:
	 * 
	 * <PRE>
	 *     [ c  s] * [a] = [sqrt(a^2+b^2)]
	 *     [-s  c]   [b]   [      0      ]
	 * </PRE>
	 *
	 * @param a
	 *            Input argument.
	 * @param b
	 *            Input argument.
	 * @param terms
	 *            A 2-element array. On output, <TT>terms[0]</TT> contains <TT>c</TT> and <TT>terms[1]</TT> contains
	 *            <TT>s</TT>.
	 *
	 * @return sqrt(<TT>a</TT><SUP>2</SUP>+<TT>b</TT><SUP>2</SUP>).
	 */
	private static double computeGivensRotation(double a, double b, double[] terms) {
		if (Math.abs(a) > Math.abs(b)) {
			double xr = b / a;
			double yr = Math.sqrt(1.0 + xr*xr);
			terms[0] = MathUtil.SIGN(1.0 / yr, a);
			terms[1] = terms[0] * xr;
			return Math.abs(a) * yr;
		} else if (b != 0.0) {
			double xr = a / b;
			double yr = Math.sqrt(1.0 + xr*xr);
			terms[1] = MathUtil.SIGN(1.0 / yr, b);
			terms[0] = terms[1] * xr;
			return Math.abs(b) * yr;
		} else {
			terms[0] = 0.0;
			terms[1] = 1.0;
			return 0.0;
		}
	}
}
