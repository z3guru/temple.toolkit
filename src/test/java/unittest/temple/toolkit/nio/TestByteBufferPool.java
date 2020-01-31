package unittest.temple.toolkit.nio;

import guru.z3.temple.toolkit.nio.ByteBufferUtils;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by jaeda on 2017. 2. 3..
 */
public class TestByteBufferPool
{
	@Test
	public void testByteRef()
	{
		byte[] b1 = new byte[5];
		byte[] b2 = new byte[5];

		byte[] pp = new byte[10] ; //ArrayUtils.addAll(b2, b1);
		ByteBuffer buf = ByteBuffer.wrap(pp);

		for ( int i = 0; i < 10; i++ )
		{
			buf.put((byte)i);
		}

		buf.flip();
		System.out.println(ByteBufferUtils.serialize(buf));
		System.out.println(ByteBufferUtils.serialize(ByteBuffer.wrap(b1)));
		System.out.println(ByteBufferUtils.serialize(ByteBuffer.wrap(b2)));
	}
}
