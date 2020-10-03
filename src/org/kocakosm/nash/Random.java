/*----------------------------------------------------------------------------*
 * This file is part of Nash-Cipher.                                          *
 * Copyright (C) 2012-2013 Osman Koçak <kocakosm@gmail.com>                   *
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

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Randomness utility methods.
 *
 * @author Osman Koçak
 */
final class Random
{
	private static final SecureRandom PRNG = new SecureRandom();

	/**
	 * Generates random bits and returns them as a {@code boolean} array.
	 * 
	 * @param n the number of bits to return.
	 * 
	 * @throws NegativeArraySizeException if {@code n} is negative.
	 */
	static boolean[] nextBits(int n)
	{
		boolean[] bits = new boolean[n];
		for (int i = 0; i < n; i++) {
			bits[i] = PRNG.nextBoolean();
		}
		return bits;
	}

	/**
	 * Shuffles the given values and returns them in a new array using the 
	 * optimized version of the Fisher-Yates shuffle algorithm (Fisher, 
	 * Yates, Durstenfeld, Knuth).
	 * 
	 * @param values the values to shuffle.
	 * 
	 * @return the shuffled array.
	 * 
	 * @throws NullPointerException if {@code values} is {@code null}.
	 */
	static int[] shuffle(int... values)
	{
		int n = values.length;
		int[] shuffled = Arrays.copyOf(values, n);
		for (int i = n; i > 1; i--) {
			swap(shuffled, i - 1, PRNG.nextInt(i));
		}
		return shuffled;
	}

	private static void swap(int[] array, int i, int j)
	{
		int v = array[i];
		array[i] = array[j];
		array[j] = v;
	}

	private Random()
	{
		/* ... */
	}
}
