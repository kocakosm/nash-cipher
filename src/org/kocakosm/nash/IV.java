/*----------------------------------------------------------------------------*
 * This file is part of Nash-Cipher.                                          *
 * Copyright © 2012-2020 Osman Koçak <kocakosm@gmail.com>                     *
 *                                                                            *
 * This program is free software. It comes without any warranty, to the       *
 * extent permitted by applicable law. You can redistribute it and/or modify  *
 * it under the terms of the Do What The Fuck You Want To Public License,     *
 * Version 2, as published by Sam Hocevar. You should have received a copy of *
 * the license along with this program. If not, see <http://www.wtfpl.net/>.  *
 *----------------------------------------------------------------------------*/

package org.kocakosm.nash;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Initialization vector. Instances of this class are immutable.
 *
 * @author Osman Koçak
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
		return Arrays.equals(bits, ((IV) o).bits);
	}
}
