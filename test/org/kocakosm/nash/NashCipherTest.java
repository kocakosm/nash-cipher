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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.kocakosm.nash.NashCipher.Mode;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link NashCipher}'s unit tests.
 *
 * @author Osman Koçak
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
	public void testConstructorWithNullKey()
	{
		Executable toTest = () -> new NashCipher(null, iv, Mode.ENCRYPTION);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testConstructorWithNullIV()
	{
		Executable toTest = () -> new NashCipher(key, null, Mode.ENCRYPTION);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testConstructorWithNullMode()
	{
		Executable toTest = () -> new NashCipher(key, iv, null);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testConstructorWithDifferentKeyAndIVSize()
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
