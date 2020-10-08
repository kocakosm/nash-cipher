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
	public void testEncryptionDecryptionRoundTrip() throws Exception
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
