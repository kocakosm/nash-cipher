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

import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A {@code NashCipherInputStream} is composed of an inner {@link InputStream}
 * and a {@link NashCipher} so that the data read from the underlying stream are
 * decrypted before being returned.
 *
 * @author Osman KOCAK
 */
public final class NashCipherInputStream extends InputStream
{
	private final NashCipher cipher;
	private final InputStream encrypted;
	private final Object lock = new Object();

	/**
	 * Creates a new {@code NashCipherInputStream}.
	 *
	 * @param key the cipher's secret key.
	 * @param encrypted the underlying stream.
	 */
	public NashCipherInputStream(Key key, InputStream encrypted)
	{
		this.cipher = new NashCipher(key, NashCipher.Mode.DECRYPTION);
		this.encrypted = new BufferedInputStream(encrypted);
	}

	@Override
	public int available() throws IOException
	{
		synchronized (lock) {
			return encrypted.available();
		}
	}

	@Override
	public void close() throws IOException
	{
		synchronized (lock) {
			encrypted.close();
		}
	}

	@Override
	public void mark(int readLimit)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean markSupported()
	{
		return false;
	}

	@Override
	public int read() throws IOException
	{
		synchronized (lock) {
			int b = encrypted.read();
			if (b < 0) {
				return b;
			}
			return cipher.process((byte)b)[0] & 0xFF;
		}
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		synchronized (lock) {
			byte[] buf = new byte[b.length];
			int len = encrypted.read(buf);
			if (len < 0) {
				return len;
			}
			byte[] processed = cipher.process(buf, 0, len);
			System.arraycopy(processed, 0, b, 0, len);
			return len;
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		synchronized (lock) {
			byte[] buf = new byte[len];
			int l = encrypted.read(buf);
			if (l < 0) {
				return l;
			}
			byte[] processed = cipher.process(buf, 0, l);
			System.arraycopy(processed, 0, b, off, l);
			return l;
		}
	}

	@Override
	public void reset() throws IOException
	{
		throw new IOException("Not supported");
	}

	@Override
	public long skip(long n) throws IOException
	{
		synchronized (lock) {
			return encrypted.skip(n);
		}
	}
}
