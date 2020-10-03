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
 * @author Osman Koçak
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
			return t.cast(ois.readObject());
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
