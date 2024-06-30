package guru.z3.toolkit.nio;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 * The ByteStreamEncoder interface.
 *
 * @param <D> the data type
 */
public interface ByteStreamEncoder<D>
{
	/**
	 * Encodes the data into a byte buffer.
	 *
	 * @param data the data
	 * @return the future byte buffer
	 */
	Future<ByteBuffer> encode(D data);
}
