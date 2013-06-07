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

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code NashCipherOutputStream} is composed of an inner {@link OutputStream}
 * and a {@link NashCipher} so that the data written to it are first encrypted
 * before being actually written to the inner stream. Instances of this class
 * are thread-safe.
 *
 * @author Osman KOCAK
 */
public final class NashCipherOutputStream extends OutputStream
{
	private final NashCipher cipher;
	private final OutputStream encrypted;
	private final Object lock = new Object();

	/**
	 * Creates a new {@code NashCipherOutputStream}.
	 *
	 * @param key the cipher's secret key.
	 * @param iv the cipher's initialization vector.
	 * @param encrypted the underlying encrypted stream.
	 *
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 * @throws IllegalArgumentException if {@code key} and {@code iv} have 
	 *	different sizes.
	 */
	public NashCipherOutputStream(Key key, IV iv, OutputStream encrypted)
	{
		if (encrypted == null) {
			throw new NullPointerException();
		}
		this.cipher = new NashCipher(key, iv, NashCipher.Mode.ENCRYPTION);
		this.encrypted = new BufferedOutputStream(encrypted);
	}

	@Override
	public void close() throws IOException
	{
		synchronized (lock) {
			encrypted.close();
		}
	}

	@Override
	public void flush() throws IOException
	{
		synchronized (lock) {
			encrypted.flush();
		}
	}

	@Override
	public void write(int i) throws IOException
	{
		synchronized (lock) {
			encrypted.write(cipher.process((byte)i));
		}
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		synchronized (lock) {
			encrypted.write(cipher.process(b, off, len));
		}
	}
}
