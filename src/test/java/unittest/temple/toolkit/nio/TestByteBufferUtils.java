/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package unittest.temple.toolkit.nio;

import guru.z3.temple.toolkit.nio.ByteBufferUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by jaeda on 2017. 4. 26..
 */
public class TestByteBufferUtils
{
	@Test
	public void testDeserialize()
	{
		String stream = "01 02 81 08 80 00 00 00 00 00 00 00 41 03 41 42 43";
		ByteBuffer buf = ByteBufferUtils.deserialize(stream);

		String test = ByteBufferUtils.serialize(buf);
		System.out.println(test);
		TestCase.assertEquals(stream.replaceAll("\\s+", ""), test);
	}

	@Test
	public void testDeserialize2()
	{
		String s = new StringBuilder()
				.append("00 00 84 00 00 0000 03 00 00 00 00 07 5F 70 72 69 76 65 74 04 5F 74 63 70 05 6C 6F 63 61 6C 00")
				.append("00 0C 00 01 00 00 0E 10 00 10 0D 5A 43 55 42 45 2D 50 52 49 4E 54 45 52 C0 0C C0 2A 00 21 80 01")
				.append("00 00 0E 10 00 22 00 00 00 00 14 E9 19 53 49 4E 49 4C 75 69 2D 4D 61 63 42 6F 6F 6B 2D 50 72 6F")
				.append("2D 6C 6F 63 61 6C C0 19 C0 2A 00 10 80 01 00 00 0E 10 00 73 74 78 74 76 65 72 73 3D 31 2C 20 74")
				.append("79 3D 5A 63 75 62 65 20 52 4E 44 20 43 6C 6F 75 64 20 50 72 69 6E 74 65 72 20 4D 6F 64 65 6C 20")
				.append("41 2C 20 75 72 6C 3D 68 74 74 70 73 3A 2F 2F 77 77 77 2E 67 6F 6F 67 6C 65 2E 63 6F 6D 2F 63 6C")
				.append("6F 75 64 70 72 696E 74 2C 20 74 79 70 65 3D 70 72 69 6E 74 65 72 2C 20 69 64 3D 2C 20 63 73 3D")
				.append("6F66666C696E65")
				.toString();
		ByteBuffer buf = ByteBufferUtils.deserialize(s);

		String test = ByteBufferUtils.serialize(buf);
		System.out.println(test);
		TestCase.assertEquals(s.replaceAll("\\s+", ""), test);
	}

}
