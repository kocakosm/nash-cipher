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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;
import org.kocakosm.nash.NashCipher.Mode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * {@link NashCipherInputStream}'s unit tests.
 *
 * @author Osman Koçak
 */
public final class NashCipherInputStreamTest
{
	private final IV iv = IV.create(8);
	private final Key key = Key.create(8);

	@Test
	public void testConstructorWithNullStream()
	{
		Executable toTest = () -> new NashCipherInputStream(key, iv, null);
		assertThrows(NullPointerException.class, toTest);
	}

	@Test
	public void testAvailable() throws Exception
	{
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
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

	@Test
	public void testMark() throws Exception
	{
		InputStream in = mock(InputStream.class);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		Executable toTest = () -> nash.mark(100);
		assertThrows(UnsupportedOperationException.class, toTest);
	}

	@Test
	public void testReset() throws Exception
	{
		InputStream in = mock(InputStream.class);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		Executable toTest = () -> nash.reset();
		assertThrows(IOException.class, toTest);
	}

	@Test
	public void testSkip() throws Exception
	{
		InputStream in = mock(InputStream.class);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		Executable toTest = () -> nash.skip(16);
		assertThrows(IOException.class, toTest);
	}

	@Test
	public void testRead() throws Exception
	{
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		InputStream in = new ByteArrayInputStream(data);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		byte[] decrypted = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			decrypted[i] = (byte) nash.read();
		}
		assertEquals(-1, nash.read());
		NashCipher cipher = new NashCipher(key, iv, Mode.DECRYPTION);
		assertArrayEquals(cipher.process(data), decrypted);
	}

	@Test
	public void testReadArray() throws Exception
	{
		byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
		InputStream in = new ByteArrayInputStream(data);
		InputStream nash = new NashCipherInputStream(key, iv, in);
		byte[] decrypted = new byte[data.length];
		nash.read(decrypted);
		assertEquals(-1, nash.read(new byte[data.length]));
		NashCipher cipher = new NashCipher(key, iv, Mode.DECRYPTION);
		assertArrayEquals(cipher.process(data), decrypted);
	}
}
