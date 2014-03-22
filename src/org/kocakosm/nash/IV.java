/*----------------------------------------------------------------------------*
 * This file is part of Nash-Cipher.                                          *
 * Copyright (C) 2012-2013 Osman KOCAK <kocakosm@gmail.com>                   *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published by   *
 * the Free Software Foundation, either version 3 of the License, or (at your *
 * option) any later version.                                                 *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public     *
 * License for more details.                                                  *
 * You should have received a copy of the GNU Lesser General Public License   *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *----------------------------------------------------------------------------*/

package org.kocakosm.nash;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Initialization vector. Instances of this class are immutable.
 *
 * @author Osman KOCAK
 */
public final class IV implements Serializable
{
	private static final long serialVersionUID = 7663912371138830514L;

	/**
	 * Creates a new random IV.
	 *
	 * @param size the size of the IV.
	 *
	 * @return the created IV.
	 *
	 * @throws IllegalArgumentException if {@code size <= 0}.
	 */
	public static IV create(int size)
	{
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		return new IV(size);
	}

	private final boolean[] bits;

	private IV(int size)
	{
		this.bits = Random.nextBits(size);
	}

	/**
	 * Returns this IV's size.
	 * 
	 * @return this IV's size.
	 */
	public int getSize()
	{
		return bits.length;
	}

	/**
	 * Returns this IV's value.
	 * 
	 * @return this IV's value.
	 */
	public boolean[] getBits()
	{
		return Arrays.copyOf(bits, bits.length);
	}

	@Override
	public String toString()
	{
		return Arrays.toString(bits);
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(bits);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) {
			return true;
		}
		if (!(o instanceof IV)) {
			return false;
		}
		final IV iv = (IV) o;
		return Arrays.equals(bits, iv.bits);
	}
}
