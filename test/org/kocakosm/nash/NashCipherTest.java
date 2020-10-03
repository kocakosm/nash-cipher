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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.kocakosm.nash.NashCipher.Mode;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

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
		NashCipher enc = new NashCipher(key, iv, Mode.ENCRYPTION);
		NashCipher dec = new NashCipher(key, iv, Mode.DECRYPTION);
		byte[] data = new byte[prng.nextInt(4096)];
		prng.nextBytes(data);

		assertArrayEquals(data, dec.process(enc.process(data)));
	}

	@Test
	public void testCreateWithNullKey()
	{
		Executable toTest = () -> new NashCipher(null, iv, Mode.ENCRYPTION);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testCreateWithNullIV()
	{
		Executable toTest = () -> new NashCipher(key, null, Mode.ENCRYPTION);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testCreateWithNullMode()
	{
		Executable toTest = () -> new NashCipher(key, iv, null);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testCreateWithDifferentKeyAndIVSize()
	{
		Executable toTest = () -> new NashCipher(
			Key.create(4), IV.create(8), Mode.DECRYPTION);
		assertThrows(IllegalArgumentException.class, toTest);
	}

	@Test
	public void testProcessWithNegativeOffset()
	{
		NashCipher enc = new NashCipher(key, iv, Mode.ENCRYPTION);
		Executable toTest = () -> enc.process(new byte[0], -1, 0);
		assertThrows(IndexOutOfBoundsException.class, toTest);
	}

	@Test
	public void testProcessWithNegativeLength()
	{
		NashCipher enc = new NashCipher(key, iv, Mode.ENCRYPTION);
		Executable toTest = () -> enc.process(new byte[0], 1, -1);
		assertThrows(IndexOutOfBoundsException.class, toTest);
	}
}
