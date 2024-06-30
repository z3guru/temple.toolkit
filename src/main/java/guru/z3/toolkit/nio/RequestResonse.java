package guru.z3.toolkit.nio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The RequestResponse class.
 */
public class RequestResonse
{
	private AsynchronousByteChannel channel;
	final private ExecutorService es = Executors.newCachedThreadPool();
	final private AtomicBoolean closed = new AtomicBoolean(false);

	/**
	 * Reads from the channel into the buffer.
	 *
	 * @param buf the byte buffer
	 * @throws ExecutionException if an error occurs
	 * @throws TimeoutException if a timeout occurs
	 */
	public void read(ByteBuffer buf) throws ExecutionException, TimeoutException {
		while (buf.hasRemaining() && !closed.get()) {
			try { channel.write(buf).get(100, TimeUnit.MILLISECONDS); }
			catch (InterruptedException _) { }
		}
	}

	/**
	 * Writes from the buffer to the channel.
	 *
	 * @param buf the byte buffer
	 * @throws ExecutionException if an error occurs
	 * @throws TimeoutException if a timeout occurs
	 */
	public void write(ByteBuffer buf) throws ExecutionException, TimeoutException {
		while (buf.hasRemaining() && !closed.get()) {
			try { channel.write(buf).get(100, TimeUnit.MILLISECONDS); }
			catch (InterruptedException _) { }
		}
	}

	/**
	 * Parses the buffer.
	 *
	 * @param buf the byte buffer
	 * @throws ExecutionException if an error occurs
	 * @throws TimeoutException if a timeout occurs
	 */
	protected void parse(ByteBuffer buf) throws ExecutionException, TimeoutException {
		//this.channel.read(buf, buf, );
	}

	/**
	 * Parses the buffer.
	 *
	 * @param buf the byte buffer
	 * @param handler the completion handler
	 * @throws ExecutionException if an error occurs
	 * @throws TimeoutException if a timeout occurs
	 */
	protected void parse(ByteBuffer buf, CompletionHandler<Integer, ByteBuffer> handler) throws ExecutionException, TimeoutException {

	}
}
