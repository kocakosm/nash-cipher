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
	 * @param mode the cipher's operation mode.
	 *
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public NashCipher(Key key, Mode mode)
	{
		if (key == null || mode == null) {
			throw new NullPointerException();
		}
		this.key = key;
		this.mode = mode;
		reset();
	}

	/** Resets this cipher. */
	public void reset()
	{
		state = key.getInitialState();
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
		if (mode == Mode.ENCRYPTION) {
			return encrypt(bytes, off, len);
		}
		if (mode == Mode.DECRYPTION) {
			return decrypt(bytes, off, len);
		}
		return null;
	}

	private byte[] encrypt(byte[] bytes, int off, int len)
	{
		byte[] encrypted = new byte[len];
		for (int i = off; i < off + len; i++) {
			byte b = bytes[i];
			int val = 0;
			for (int j = 0; j < 8; j++) {
				boolean in = ((b & 0xFF) & (1 << j)) > 0;
				boolean out = xor(in, state[state.length - 1]);
				val = (val >>> 1) | (out ? 0x80 : 0);
				process(out);
			}
			encrypted[i] = (byte)val;
		}
		return encrypted;
	}

	private byte[] decrypt(byte[] bytes, int off, int len)
	{
		byte[] decrypted = new byte[len];
		for (int i = off; i < off + len; i++) {
			byte b = bytes[i];
			int val = 0;
			for (int j = 0; j < 8; j++) {
				boolean in = ((b & 0xFF) & (1 << j)) > 0;
				boolean out = xor(in, state[state.length - 1]);
				val = (val >>> 1) | (out ? 0x80 : 0);
				process(in);
			}
			decrypted[i] = (byte)val;
		}
		return decrypted;
	}

	private void process(boolean bit)
	{
		int[] permutations;
		boolean[] bits;
		if (bit) {
			permutations = key.getRedPermutations();
			bits = key.getRedBits();
		} else {
			permutations = key.getBluePermutations();
			bits = key.getBlueBits();
		}
		for (int i = 0; i < state.length; i++) {
			state[i] = xor(state[permutations[i]], bits[i]);
		}
		state[0] = bit;
	}

	private boolean xor(boolean a, boolean b)
	{
		if ((a && !b) || (!a && b)) {
			return true;
		}
		return false;
	}
}
