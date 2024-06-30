package guru.z3.toolkit.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

/**
 * <pre>
 *      START_CODE  : 2bytes
 *      SEQ_NO      : 4bytes  (if 4E)
 *      NETWORK_NO  : 1byte
 *      PC_NO       : 1byte
 *      IO_NO       : 2byte
 *      STN_NO      : 1byte
 *      Length      : 2bytes
 *      Timer/Endcode: 2bytes
 *
 * </pre>
 *
 * @param <M> the message type
 */
public class MessageParser<M> implements CompletionHandler<Integer, ByteBuffer>
{
	final private ExecutorService es = Executors.newSingleThreadExecutor();
	final private ByteBuffer buffer = ByteBuffer.allocate(1024);

	private int state;

	/**
	 * Parses a message from the channel.
	 *
	 * @param ch the asynchronous byte channel
	 * @return the future message
	 */
	public Future<M> parse(AsynchronousByteChannel ch) {
		this.buffer.clear();
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);

		long timeout = 1000L;

		return es.submit(() -> {
			try {
				var count = ch.read(this.buffer).get(timeout, TimeUnit.MILLISECONDS);
				if (count < 2) count = ch.read(this.buffer).get(1, TimeUnit.MILLISECONDS);

				// check 3E/4E and req/res
				this.buffer.flip();
				var startCode = this.buffer.getShort() & 0xFFFF;

				//
				if (!checkRemaining(4, ch, this.buffer, timeout, TimeUnit.MILLISECONDS)) return null;
				if (!checkRemaining(7, ch, this.buffer, timeout, TimeUnit.MILLISECONDS)) return null;

				///


			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
			catch (TimeoutException e) {
				throw new RuntimeException(e);
			}

			return null;
		});
	}

	/**
	 * Checks if the buffer has enough remaining bytes.
	 *
	 * @param remaining the remaining bytes
	 * @param ch the asynchronous byte channel
	 * @param buffer the byte buffer
	 * @param timeout the timeout
	 * @param unit the time unit
	 * @return true if the buffer has enough remaining bytes, false otherwise
	 * @throws InterruptedException if interrupted
	 * @throws ExecutionException if an error occurs
	 * @throws TimeoutException if a timeout occurs
	 */
	private boolean checkRemaining(int remaining, AsynchronousByteChannel ch, ByteBuffer buffer, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		while (this.buffer.remaining() < remaining) {
			this.buffer.compact();
			ch.read(this.buffer).get(timeout, unit);
			this.buffer.flip();
		}

		return true;
	}

	/**
	 * Called when the read operation is completed.
	 *
	 * @param count the number of bytes read
	 * @param buf the byte buffer
	 */
	@Override
	public void completed(Integer count, ByteBuffer buf) {

	}

	/**
	 * Called when the read operation fails.
	 *
	 * @param exc the exception
	 * @param buf the byte buffer
	 */
	@Override
	public void failed(Throwable exc, ByteBuffer buf) {

	}

	/**
	 * The MessageHandler interface.
	 *
	 * @param <M> the message type
	 */
	public interface MessageHandler<M> {

	}
}
