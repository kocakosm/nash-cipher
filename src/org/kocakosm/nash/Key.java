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
 * Secret key for Nash ciphers. Instances of this class are immutable.
 *
 * @author Osman KOCAK
 */
public final class Key implements Serializable
{
	private static final long serialVersionUID = 84530924754951016L;

	/**
	 * Creates a new random key.
	 *
	 * @param size the size of the key (actually, the permuter's size).
	 *
	 * @return the created secret key.
	 *
	 * @throws IllegalArgumentException if {@code size <= 0}.
	 */
	public static Key create(int size)
	{
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		return new Key(size);
	}

	private final boolean[] redBits;
	private final boolean[] blueBits;
	private final int[] redPermutations;
	private final int[] bluePermutations;

	private Key(int size)
	{
		this.redBits = Random.nextBits(size);
		this.blueBits = Random.nextBits(size);
		this.redPermutations = generatePermutations(size);
		this.bluePermutations = generatePermutations(size);
	}

	/**
	 * Returns the size of the key (actually, the permuter's size).
	 * 
	 * @return the size of the key (actually, the permuter's size).
	 */
	public int getSize()
	{
		return redBits.length;
	}

	/**
	 * Returns the blue complementation table.
	 *
	 * @return the blue complementation table.
	 */
	public boolean[] getBlueBits()
	{
		return Arrays.copyOf(blueBits, blueBits.length);
	}

	/**
	 * Returns the blue permutations table.
	 *
	 * @return the blue permutations table.
	 */
	public int[] getBluePermutations()
	{
		return Arrays.copyOf(bluePermutations, bluePermutations.length);
	}

	/**
	 * Returns the red complementation table.
	 *
	 * @return the red complementation table.
	 */
	public boolean[] getRedBits()
	{
		return Arrays.copyOf(redBits, redBits.length);
	}

	/**
	 * Returns the red permutations table.
	 * 
	 * @return the red permutations table.
	 */
	public int[] getRedPermutations()
	{
		return Arrays.copyOf(redPermutations, redPermutations.length);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) {
			return true;
		}
		if (!(o instanceof Key)) {
			return false;
		}
		final Key k = (Key)o;
		return Arrays.equals(redBits, k.redBits)
			&& Arrays.equals(blueBits, k.blueBits)
			&& Arrays.equals(redPermutations, k.redPermutations)
			&& Arrays.equals(bluePermutations, k.bluePermutations);
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 89 * hash + Arrays.hashCode(redPermutations);
		hash = 89 * hash + Arrays.hashCode(bluePermutations);
		return hash;
	}

	private int[] generatePermutations(int size)
	{
		int[] values = new int[size];
		for (int i = 0; i < size - 1; i++) {
			values[i] = i;
		}
		values[size - 1] = 0;
		return Random.shuffle(values);
	}
}
