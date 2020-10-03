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

package org.kocakosm.nash.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link NashCipherOutputStream}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class NashCipherOutputStreamTest
{
	private final IV iv = IV.create(8);
	private final Key key = Key.create(8);

	@Test
	public void testCreateWithNullStream()
	{
		Executable toTest = () -> new NashCipherOutputStream(key, iv, null);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testClose() throws Exception
	{
		OutputStream out = mock(OutputStream.class);
		new NashCipherOutputStream(key, iv, out).close();
		verify(out).close();
	}

	@Test
	public void testFlush() throws Exception
	{
		OutputStream out = mock(OutputStream.class);
		new NashCipherOutputStream(key, iv, out).flush();
		verify(out).flush();
	}

	@Test
	public void testWrite() throws Exception
	{
		byte[] data = "Hello".getBytes("UTF-8");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream nash = new NashCipherOutputStream(key, iv, out);
		for (byte b : data) {
			nash.write(b);
		}
		nash.flush();
		NashCipher cipher = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		assertArrayEquals(cipher.process(data), out.toByteArray());
	}

	@Test
	public void testWriteArray() throws Exception
	{
		byte[] data = "Hello".getBytes("UTF-8");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream nash = new NashCipherOutputStream(key, iv, out);
		nash.write(data);
		nash.flush();
		NashCipher cipher = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		assertArrayEquals(cipher.process(data), out.toByteArray());
	}
}
