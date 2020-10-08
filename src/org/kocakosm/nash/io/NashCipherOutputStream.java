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

import org.kocakosm.nash.IV;
import org.kocakosm.nash.Key;
import org.kocakosm.nash.NashCipher;
import org.kocakosm.nash.NashCipher.Mode;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * A {@code NashCipherOutputStream} is composed of an inner {@link OutputStream}
 * and a {@link NashCipher} so that the data written to it are first encrypted
 * before being actually written to the inner stream. Instances of this class
 * are thread-safe.
 *
 * @author Osman Koçak
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
		Objects.requireNonNull(encrypted);
		this.cipher = new NashCipher(key, iv, Mode.ENCRYPTION);
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
			encrypted.write(cipher.process((byte) i));
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
