package guru.z3.toolkit.nio;

import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 * The ByteStreamDecoder interface.
 *
 * @param <D> the decoded type
 */
public interface ByteStreamDecoder<D>
{
	/**
	 * Decodes the byte buffer.
	 *
	 * @param buffer the byte buffer
	 * @return the future decoded object
	 */
	Future<D> decode(ByteBuffer buffer);
}
