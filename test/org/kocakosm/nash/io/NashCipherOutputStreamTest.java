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

package org.kocakosm.nash.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;
import org.kocakosm.nash.NashCipher.Mode;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link NashCipherOutputStream}'s unit tests.
 *
 * @author Osman Koçak
 */
public final class NashCipherOutputStreamTest
{
	private final IV iv = IV.create(8);
	private final Key key = Key.create(8);

	@Test
	public void testConstructorWithNullStream()
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
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream nash = new NashCipherOutputStream(key, iv, out);
		for (byte b : data) {
			nash.write(b);
		}
		nash.flush();
		NashCipher cipher = new NashCipher(key, iv, Mode.ENCRYPTION);
		assertArrayEquals(cipher.process(data), out.toByteArray());
	}

	@Test
	public void testWriteArray() throws Exception
	{
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStream nash = new NashCipherOutputStream(key, iv, out);
		nash.write(data);
		nash.flush();
		NashCipher cipher = new NashCipher(key, iv, Mode.ENCRYPTION);
		assertArrayEquals(cipher.process(data), out.toByteArray());
	}
}
