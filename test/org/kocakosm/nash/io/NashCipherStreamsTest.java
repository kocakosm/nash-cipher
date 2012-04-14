/*----------------------------------------------------------------------------*
 * This file is part of Nash-Cipher.                                          *
 * Copyright (C) 2012 Osman KOCAK <kocakosm@gmail.com>                        *
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

package org.kocakosm.nash.io;

import static org.junit.Assert.assertArrayEquals;

import org.kocakosm.nash.Key;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Test;

/**
 * Nash cipher streams tests.
 *
 * @author Osman KOCAK
 */
public final class NashCipherStreamsTest
{
	@Test
	public void test() throws Exception
	{
		Random rnd = new Random();
		byte[] data = new byte[rnd.nextInt(4096)];
		rnd.nextBytes(data);

		Key secret = Key.create();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream out = new NashCipherOutputStream(secret, baos);
		out.write(data);
		out.flush();

		InputStream bais = new ByteArrayInputStream(baos.toByteArray());
		InputStream in = new NashCipherInputStream(secret, bais);
		byte[] data2 = new byte[data.length];
		in.read(data2);

		assertArrayEquals(data, data2);
	}
}
