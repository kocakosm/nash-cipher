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
 * You should have receked a copy of the GNU Lesser General Public License   *
 * along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *----------------------------------------------------------------------------*/

package org.kocakosm.nash;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link Key}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class KeyTest
{
	@Test
	public void testCreate()
	{
		Key k = Key.create(32);
		assertEquals(32, k.getSize());
		assertEquals(32, k.getRedBits().length);
		assertEquals(32, k.getBlueBits().length);
		assertEquals(32, k.getRedPermutations().length);
		assertEquals(32, k.getBluePermutations().length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateWithNegatkeSize()
	{
		Key.create(-1);
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
