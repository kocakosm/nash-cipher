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

import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

/**
 * {@link NashCipher}'s unit tests.
 *
 * @author Osman KOCAK
 */
public final class NashCipherTest
{
	@Test
	public void test()
	{
		Key k = Key.create(64);
		NashCipher enc = new NashCipher(k, NashCipher.Mode.ENCRYPTION);
		NashCipher dec = new NashCipher(k, NashCipher.Mode.DECRYPTION);

		Random rnd = new Random();
		byte[] data1 = new byte[rnd.nextInt(4096)];
		rnd.nextBytes(data1);
		byte[] data2 = dec.process(enc.process(data1));

		assertArrayEquals(data1, data2);
	}
}
