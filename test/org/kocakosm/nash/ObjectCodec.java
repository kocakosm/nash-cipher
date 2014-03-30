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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Serialization utilities.
 *
 * @author Osman KOCAK
 */
final class ObjectCodec
{
	static byte[] encode(Serializable object) throws IOException
	{
		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(out);
			oos.writeObject(object);
			oos.flush();
			return out.toByteArray();
		} finally {
			close(oos);
		}
	}

	static <T extends Serializable> T decode(byte[] object, Class<T> t)
		throws IOException
	{
		ObjectInputStream ois = null;
		ByteArrayInputStream in = new ByteArrayInputStream(object);
		try {
			ois = new ObjectInputStream(in);
			return (T) ois.readObject();
		} catch (ClassNotFoundException ex) {
			throw new IOException(ex);
		} finally {
			close(ois);
		}
	}

	private static void close(Closeable stream) throws IOException
	{
		if (stream != null) {
			stream.close();
		}
	}

	private ObjectCodec()
	{
		/* ... */
	}
}
