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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link IV}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class IVTest
{
	@Test
	public void testCreate()
	{
		IV iv = IV.create(32);
		assertEquals(32, iv.getSize());
		assertEquals(32, iv.getBits().length);
	}

	@Test
	public void testCreateWithNegativeSize()
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
