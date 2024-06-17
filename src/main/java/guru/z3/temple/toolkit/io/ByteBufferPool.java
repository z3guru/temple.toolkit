package guru.z3.temple.toolkit.io;

import java.nio.ByteBuffer;

/**
 * {@link ByteBuffer}에서 사용할 메모리를 확보한 후, 필요시 할당하고 회수해서 재사용되도록
 * 하는 객체
 *
 */
public class ByteBufferPool
{
	/**
	 * 확보된 메모리 영역 이중배열로 하며
	 */
	private byte[][] buffer;


	public ByteBuffer lease()
	{
		return null;
	}

}
