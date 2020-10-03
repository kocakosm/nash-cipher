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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link Key}'s unit tests.
 *
 * @author Osman Koçak
 */
public final class KeyTest
{
	@Test
	public void testConstructor()
	{
		Key k = Key.create(32);
		assertEquals(32, k.getSize());
		assertEquals(32, k.getRedBits().length);
		assertEquals(32, k.getBlueBits().length);
		assertEquals(32, k.getRedPermutations().length);
		assertEquals(32, k.getBluePermutations().length);
	}

	@Test
	public void testConstructorWithNegativeSize()
	{
		Executable toTest = () -> Key.create(-1);
		assertThrows(IllegalArgumentException.class, toTest);
	}

	@Test
	public void testEqualsAndHashCode()
	{
		Key k = Key.create(32);
		assertFalse(k.equals(Key.create(16)));
		assertFalse(k.equals(Key.create(32)));
		assertTrue(k.equals(k));
		assertEquals(k.hashCode(), k.hashCode());
		assertFalse(k.equals((Key) (null)));
	}

	@Test
	public void testSerialization() throws Exception
	{
		Key k = Key.create(32);
		Key decoded = ObjectCodec.decode(ObjectCodec.encode(k), Key.class);
		assertNotSame(k, decoded);
		assertEquals(k, decoded);
	}
}
