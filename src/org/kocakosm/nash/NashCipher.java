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

package org.kocakosm.nash;

/**
 * Nash's cipher. Instances of this class are not thread-safe.
 *
 * @author Osman KOCAK
 */
public final class NashCipher
{
	/** Cipher's operation mode. */
	public enum Mode
	{
		/** Encryption mode. */
		ENCRYPTION,

		/** Decryption mode. */
		DECRYPTION
	}

	private final Key key;
	private final Mode mode;
	private boolean[] state;

	/**
	 * Creates a new {@code NashCipher}.
	 *
	 * @param key the cipher's secret key.
	 * @param iv the cipher's initialization vector.
	 * @param mode the cipher's operation mode.
	 *
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 * @throws IllegalArgumentException if {@code key} and {@code iv} have
	 *	different sizes.
	 */
	public NashCipher(Key key, IV iv, Mode mode)
	{
		if (key == null || mode == null) {
			throw new NullPointerException();
		}
		this.key = key;
		this.mode = mode;
		reset(iv);
	}

	/**
	 * Resets this cipher.
	 *
	 * @param iv the initialization vector.
	 *
	 * @throws NullPointerException if {@code iv} is {@code null}.
	 * @throws IllegalArgumentException if {@code iv}'s size doesn't match
	 *	the size of the cipher's key.
	 */
	public void reset(IV iv)
	{
		if (key.getSize() != iv.getSize()) {
			throw new IllegalArgumentException();
		}
		state = iv.getBits();
	}

	/**
	 * Processes the given data bytes.
	 *
	 * @param bytes the data to process.
	 *
	 * @return the processed data.
	 *
	 * @throws NullPointerException if {@code bytes} is {@code null}.
	 */
	public byte[] process(byte... bytes)
	{
		return process(bytes, 0, bytes.length);
	}

	/**
	 * Processes the given data bytes.
	 *
	 * @param bytes the data to process.
	 * @param off the input offset.
	 * @param len the number of bytes to process.
	 *
	 * @return the processed data.
	 *
	 * @throws NullPointerException if {@code bytes} is {@code null}.
	 * @throws IndexOutOfBoundsException if {@code off} or {@code len} is
	 *	negative or if {@code off + len} is greater than {@code bytes}'
	 *	length.
	 */
	public byte[] process(byte[] bytes, int off, int len)
	{
		if (off < 0 || len < 0) {
			throw new IndexOutOfBoundsException();
		}
		byte[] processed = new byte[len];
		for (int i = off; i < off + len; i++) {
			int val = 0;
			for (int j = 0; j < 8; j++) {
				boolean in = ((bytes[i] & 0xFF) & (1 << j)) > 0;
				boolean out = in ^ state[state.length - 1];
				val = (val >>> 1) | (out ? 0x80 : 0);
				process(mode == Mode.DECRYPTION ? in : out);
			}
			processed[i] = (byte) val;
		}
		return processed;
	}

	private void process(boolean bit)
	{
		boolean[] bits;
		int[] permutations;
		if (bit) {
			bits = key.getRedBits();
			permutations = key.getRedPermutations();
		} else {
			bits = key.getBlueBits();
			permutations = key.getBluePermutations();
		}
		for (int i = 1; i < state.length; i++) {
			state[i] = state[permutations[i]] ^ bits[i];
		}
		state[0] = bit;
	}
}
