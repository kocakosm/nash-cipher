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

package org.kocakosm.nash.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Nash cipher streams tests.
 *
 * @author Osman Koçak
 */
public final class NashCipherStreamsTest
{
	@Test
	public void testRoundtrip() throws Exception
	{
		Random prng = new Random();
		byte[] data = new byte[prng.nextInt(4096)];
		prng.nextBytes(data);
		IV iv = IV.create(64);
		Key secret = Key.create(64);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream out = new NashCipherOutputStream(secret, iv, baos);
		out.write(data);
		out.flush();

		InputStream bais = new ByteArrayInputStream(baos.toByteArray());
		InputStream in = new NashCipherInputStream(secret, iv, bais);
		byte[] decrypted = new byte[data.length];
		in.read(decrypted);

		assertArrayEquals(data, decrypted);
	}
}
