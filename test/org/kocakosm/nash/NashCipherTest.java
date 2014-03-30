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

import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

/**
 * {@link NashCipher}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class NashCipherTest
{
	private final IV iv = IV.create(64);
	private final Key key = Key.create(64);
	private final Random prng = new Random();

	@Test
	public void testCipher()
	{
		NashCipher enc = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		NashCipher dec = new NashCipher(key, iv, NashCipher.Mode.DECRYPTION);
		byte[] data = new byte[prng.nextInt(4096)];
		prng.nextBytes(data);

		assertArrayEquals(data, dec.process(enc.process(data)));
	}

	@Test(expected = NullPointerException.class)
	public void testCreateWithNullKey()
	{
		new NashCipher(null, iv, NashCipher.Mode.ENCRYPTION);
	}

	@Test(expected = NullPointerException.class)
	public void testCreateWithNullIV()
	{
		new NashCipher(key, null, NashCipher.Mode.ENCRYPTION);
	}

	@Test(expected = NullPointerException.class)
	public void testCreateWithNullMode()
	{
		new NashCipher(key, iv, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithDifferentKeyAndIVSize()
	{
		new NashCipher(Key.create(4), IV.create(8), NashCipher.Mode.DECRYPTION);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testProcessWithNegativeOffset()
	{
		NashCipher enc = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		enc.process(new byte[0], -1, 0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testProcessWithNegativeLength()
	{
		NashCipher enc = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		enc.process(new byte[0], 1, -1);
	}
}
