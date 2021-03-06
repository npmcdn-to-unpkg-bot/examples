//******************************************************************************
//
// File:    ArrayXYSeries.java
// Package: edu.rit.numeric
// Unit:    Class edu.rit.numeric.ArrayXYSeries
//
// This Java source file is copyright (C) 2008 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is part of the Parallel Java Library ("PJ"). PJ is free
// software; you can redistribute it and/or modify it under the terms of the GNU
// General Public License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
//
// PJ is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

package edu.rit.numeric;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Class ArrayXYSeries provides an {@linkplain XYSeries} view of values stored
 * in arrays. Changing the contents of the arrays will change the ArrayXYSeries.
 *
 * @author  Alan Kaminsky
 * @version 17-Jun-2008
 */
public class ArrayXYSeries
	extends XYSeries
	implements Externalizable
	{

// Hidden data members.

	private static final long serialVersionUID = 4402374363889012094L;

	private double[] xarray;
	private double[] yarray;

// Exported constructors.

	/**
	 * Construct a new uninitialized array X-Y series. This constructor is for
	 * use only by object deserialization.
	 */
	public ArrayXYSeries()
		{
		}

	/**
	 * Construct a new array X-Y series.
	 *
	 * @param  xarray  Array of X values.
	 * @param  yarray  Array of Y values.
	 *
	 * @exception  NullPointerException
	 *     (unchecked exception) Thrown if <TT>xarray</TT> is null or
	 *     <TT>yarray</TT> is null.
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>xarray</TT> and <TT>yarray</TT>
	 *     are not the same length.
	 */
	public ArrayXYSeries
		(double[] xarray,
		 double[] yarray)
		{
		if (xarray == null)
			{
			throw new NullPointerException
				("ArrayXYSeries(): xarray is null");
			}
		if (yarray == null)
			{
			throw new NullPointerException
				("ArrayXYSeries(): yarray is null");
			}
		if (xarray.length != yarray.length)
			{
			throw new IllegalArgumentException
				("ArrayXYSeries(): xarray length (= "+xarray.length+
				 ") != yarray length (= "+yarray.length+"), illegal");
			}
		this.xarray = xarray;
		this.yarray = yarray;
		}

// Exported operations.

	/**
	 * Returns the number of values in this series.
	 *
	 * @return  Length.
	 */
	public int length()
		{
		return xarray.length;
		}

	/**
	 * Returns the given X value in this series.
	 *
	 * @param  i  Index.
	 *
	 * @return  The X value in this series at index <TT>i</TT>.
	 *
	 * @exception  ArrayIndexOutOfBoundsException
	 *     (unchecked exception) Thrown if <TT>i</TT> is not in the range
	 *     <TT>0</TT> .. <TT>length()-1</TT>.
	 */
	public double x
		(int i)
		{
		return xarray[i];
		}

	/**
	 * Returns the given Y value in this series.
	 *
	 * @param  i  Index.
	 *
	 * @return  The Y value in this series at index <TT>i</TT>.
	 *
	 * @exception  ArrayIndexOutOfBoundsException
	 *     (unchecked exception) Thrown if <TT>i</TT> is not in the range
	 *     <TT>0</TT> .. <TT>length()-1</TT>.
	 */
	public double y
		(int i)
		{
		return yarray[i];
		}

	/**
	 * Returns a {@linkplain Series} view of the X values in this XY series.
	 * <P>
	 * <I>Note:</I> The returned Series object is backed by this XY series
	 * object. Changing the contents of this XY series object will change the
	 * contents of the returned Series object.
	 *
	 * @return  Series of X values.
	 */
	public Series xSeries()
		{
		return new ArraySeries (xarray);
		}

	/**
	 * Returns a {@linkplain Series} view of the Y values in this XY series.
	 * <P>
	 * <I>Note:</I> The returned Series object is backed by this XY series
	 * object. Changing the contents of this XY series object will change the
	 * contents of the returned Series object.
	 *
	 * @return  Series of Y values.
	 */
	public Series ySeries()
		{
		return new ArraySeries (yarray);
		}

	/**
	 * Write this X-Y series to the given object output stream.
	 *
	 * @param  out  Object output stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void writeExternal
		(ObjectOutput out)
		throws IOException
		{
		int n = xarray.length;
		out.writeInt (n);
		for (int i = 0; i < n; ++ i)
			{
			out.writeDouble (xarray[i]);
			out.writeDouble (yarray[i]);
			}
		}

	/**
	 * Read this X-Y series from the given object input stream.
	 *
	 * @param  in  Object input stream.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void readExternal
		(ObjectInput in)
		throws IOException
		{
		int n = in.readInt();
		xarray = new double [n];
		yarray = new double [n];
		for (int i = 0; i < n; ++ i)
			{
			xarray[i] = in.readDouble();
			yarray[i] = in.readDouble();
			}
		}

	}
