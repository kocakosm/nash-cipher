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
 * {@link IV}'s unit tests.
 *
 * @author Osman Koçak
 */
public final class IVTest
{
	@Test
	public void testConstructor()
	{
		IV iv = IV.create(32);
		assertEquals(32, iv.getSize());
		assertEquals(32, iv.getBits().length);
	}

	@Test
	public void testConstructorWithNegativeSize()
	{
		Executable toTest = () -> IV.create(-1);
		assertThrows(IllegalArgumentException.class, toTest);
	}

	@Test
	public void testEqualsAndHashCode()
	{
		IV iv = IV.create(32);
		assertFalse(iv.equals(IV.create(16)));
		assertFalse(iv.equals(IV.create(32)));
		assertTrue(iv.equals(iv));
		assertEquals(iv.hashCode(), iv.hashCode());
		assertFalse(iv.equals((IV) (null)));
	}

	@Test
	public void testSerialization() throws Exception
	{
		IV iv = IV.create(32);
		IV decoded = ObjectCodec.decode(ObjectCodec.encode(iv), IV.class);
		assertNotSame(iv, decoded);
		assertEquals(iv, decoded);
	}
}
