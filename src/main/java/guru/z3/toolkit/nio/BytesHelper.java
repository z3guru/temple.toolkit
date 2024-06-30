package guru.z3.toolkit.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A helper class for byte array manipulations.
 */
public class BytesHelper
{
	/**
	 * Converts a little-endian byte array segment to an integer.
	 *
	 * @param bytes  The source byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 * @return The resulting integer value.
	 */
	static public int bytes2IntLE(byte[] bytes, int offset, int length) {
		var val = 0;
		var end = offset + length;
		for (var i = offset; i < end; i++) val = (val << 8) | (bytes[i] & 0xFF);

		return val;
	}

	/**
	 * Converts a big-endian byte array segment to an integer.
	 *
	 * @param bytes  The source byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 * @return The resulting integer value.
	 */
	static public int bytes2IntBE(byte[] bytes, int offset, int length) {
		var val = 0;
		var end = offset + length - 1;
		for (var i = end; i >= offset; i--) val = (val << 8) | (bytes[i] & 0xFF);

		return val;
	}

	/**
	 * Converts a little-endian byte array segment to a long.
	 *
	 * @param bytes  The source byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 * @return The resulting long value.
	 */
	static public long bytes2LongLE(byte[] bytes, int offset, int length) {
		var val = 0L;
		var end = offset + length;
		for (var i = offset; i < end; i++) val = (val << 8) | (bytes[i] & 0xFF);

		return val;
	}

	/**
	 * Converts a big-endian byte array segment to a long.
	 *
	 * @param bytes  The source byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 * @return The resulting long value.
	 */
	static public long bytes2LongBE(byte[] bytes, int offset, int length) {
		var val = 0L;
		var end = offset + length - 1;
		for (var i = end; i >= offset; i--) val = (val << 8) | (bytes[i] & 0xFF);

		return val;
	}

	/**
	 * Converts an integer to a little-endian byte array segment.
	 *
	 * @param source The source integer.
	 * @param bytes  The target byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 */
	static public void int2BytesLE(int source, byte[] bytes, int offset, int length) {
		var end = offset + length - 1;
		for (var i = end; i >= offset; i--) {
			bytes[i] = (byte)(source & 0xFF);
			source >>= 8;
		}
	}

	/**
	 * Converts an integer to a big-endian byte array segment.
	 *
	 * @param source The source integer.
	 * @param bytes  The target byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 */
	static public void int2BytesBE(int source, byte[] bytes, int offset, int length) {
		var end = offset + length;
		for (var i = offset; i < end; i++) {
			bytes[i] = (byte)(source & 0xFF);
			source >>= 8;
		}
	}

	/**
	 * Converts a long to a little-endian byte array segment.
	 *
	 * @param source The source long.
	 * @param bytes  The target byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 */
	static public void long2BytesLE(long source, byte[] bytes, int offset, int length) {
		var end = offset + length - 1;
		for (var i = end; i >= offset; i--) {
			bytes[i] = (byte)(source & 0xFF);
			source >>= 8;
		}
	}

	/**
	 * Converts a long to a big-endian byte array segment.
	 *
	 * @param source The source long.
	 * @param bytes  The target byte array.
	 * @param offset The starting offset in the byte array.
	 * @param length The number of bytes to convert.
	 */
	static public void long2BytesBE(long source, byte[] bytes, int offset, int length) {
		var end = offset + length;
		for (var i = offset; i < end; i++) {
			bytes[i] = (byte)(source & 0xFF);
			source >>= 8;
		}
	}

	/**
	 * 버퍼의 바이트 정보들을 16진수 문자열로 변환한다.
	 * @param buf 대상 버퍼
	 * @return 16진수 문자열
	 */
	public static String serialize(ByteBuffer buf)
	{
		ByteBuffer dbuf = buf.duplicate();
		StringBuilder text = new StringBuilder();
		while(dbuf.hasRemaining())
		{
			byte b = dbuf.get();
			text.append(encode((b >> 4) & 0x0F));
			text.append(encode(b & 0x0F));
		}
		return text.toString();
	}

	/**
	 * 일련의 16진수 문자열을 해석해서 {@link ByteBuffer}로 변환
	 */
	public static ByteBuffer deserialize(String hex)
	{
		String hh = hex.replaceAll("^[0-9a-fA-F]", "");
		int length = hh.length() >> 1;
		ByteBuffer buf = ByteBuffer.allocate(length);
		for (int idx = 0; idx < length; idx++)
		{
			buf.put((byte) Integer.parseInt(hh.substring(2 * idx, 2 * idx + 2), 16));
		}
		buf.flip();
		return buf;
	}

	/**
	 * 0~15까지의 값을 16진수 문자로 변환
	 */
	private static char encode(int val)
	{
		if (val < 10) return (char)('0' + val);
		else if (val < 16) return (char)('A' + val - 10);
		else return 'X';
	}

	/**
	 * 버퍼의 바이트 정보들을 16진수 문자열로 변환한다.
	 * @param buf 표현하고자 하는 버퍼
	 * @param count 한줄마다 표현할 바이트 수
	 * @return 구성된 문자열
	 */
	public static String toHexString(ByteBuffer buf, int count)
	{
		ByteBuffer dbuf = buf.duplicate();

		int hexpart = 3 * count;
		char[] line = new char[hexpart + count];
		StringBuffer text = new StringBuffer();

		int seg = 0;
		int idx1 = 0;
		int idx2 = 0;
		int idx3 = hexpart;

		while ( dbuf.hasRemaining() )
		{
			if ( ++idx1 > count )
			{
				idx1 = 1;
				idx2 = 0;
				idx3 = hexpart;
				text.append(String.format("%08X", seg)).append(": ").append(line).append("\n");
				seg += count;
			}

			byte b = dbuf.get();
			int val = (b & 0xF0) >> 4;
			line[idx2++] = (char)((val > 9) ? 'A' + (val - 10) : '0' + val);
			val = b & 0x0F;
			line[idx2++] = (char)((val > 9) ? 'A' + (val - 10) : '0' + val);
			line[idx2++] = ' ';
			line[idx3++] = (b >= 0x20 && b <= 0x7E) ? (char)b : '.';
		}

		if ( idx1 > 0 )
		{
			for ( int i = idx2; i < hexpart; i++ ) line[i] = ' ';
			text.append(String.format("%08X", seg)).append(": ").append(new String(line, 0, line.length - (count - idx1))).append("\n");
		}

		return text.toString();
	}

	public static int getInt(ByteBuffer buf, int count, ByteOrder order)
	{
		int num = 0;

		if ( order == ByteOrder.BIG_ENDIAN )
		{
			for ( int i = count - 1; i >= 0; i-- ) num |= buf.get() << (8 * i);
		}
		else
		{
			for ( int i = 0; i < count; i++ ) num |= buf.get() << (8 * i);
		}

		return num;
	}

}
