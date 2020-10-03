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
