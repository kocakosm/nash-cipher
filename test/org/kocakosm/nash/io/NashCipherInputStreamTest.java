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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * {@link NashCipherInputStream}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class NashCipherInputStreamTest
{
	private final IV iv = IV.create(8);
	private final Key key = Key.create(8);

	@Test(expected = NullPointerException.class)
	public void testCreateWithNullStream()
	{
		new NashCipherInputStream(key, iv, null);
	}

	@Test
	public void testAvailable() throws Exception
	{
		byte[] data = "Hello".getBytes("UTF-8");
		InputStream in = new ByteArrayInputStream(data);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		assertEquals(in.available(), nash.available());
	}

	@Test
	public void testClose() throws Exception
	{
		InputStream in = mock(InputStream.class);
		new NashCipherInputStream(key, iv, in).close();
		verify(in).close();
	}

	@Test
	public void testMarkSupported() throws Exception
	{
		InputStream in = mock(InputStream.class);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		assertFalse(nash.markSupported());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testMark() throws Exception
	{
		InputStream in = mock(InputStream.class);
		new NashCipherInputStream(key, iv, in).mark(100);
	}

	@Test(expected = IOException.class)
	public void testReset() throws Exception
	{
		InputStream in = mock(InputStream.class);
		new NashCipherInputStream(key, iv, in).reset();
	}

	@Test(expected = IOException.class)
	public void testSkip() throws Exception
	{
		InputStream in = mock(InputStream.class);
		new NashCipherInputStream(key, iv, in).skip(16);
	}

	@Test
	public void testRead() throws Exception
	{
		byte[] data = "Hello".getBytes("UTF-8");
		InputStream in = new ByteArrayInputStream(data);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		byte[] decrypted = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			decrypted[i] = (byte) nash.read();
		}
		assertEquals(-1, nash.read());
		NashCipher cipher = new NashCipher(key, iv, NashCipher.Mode.DECRYPTION);
		assertArrayEquals(cipher.process(data), decrypted);
	}

	@Test
	public void testReadArray() throws Exception
	{
		byte[] data = "Hello".getBytes("UTF-8");
		InputStream in = new ByteArrayInputStream(data);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		byte[] decrypted = new byte[data.length];
		nash.read(decrypted);
		assertEquals(-1, nash.read(new byte[data.length]));
		NashCipher cipher = new NashCipher(key, iv, NashCipher.Mode.DECRYPTION);
		assertArrayEquals(cipher.process(data), decrypted);
	}
}
