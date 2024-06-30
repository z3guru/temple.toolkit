// Java
package guru.z3.toolkit.nio;

import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A pool for managing multiple {@link AsynchronousSocketChannel} connections via {@link Transceiver.Peer}.
 * This class provides functionalities to establish asynchronous connections, lease and release channels,
 * and handle automatic reconnection for failed or discarded channels.
 * </p>
 * Connection management strategy:
 * - <b>Active Pool:</b> Maintains a {@link BlockingDeque} of connected {@link Transceiver.Peer} instances available for immediate use.
 * - <b>Reconnection Mechanism:</b> Instead of a separate "disconnected" pool, failed or trashed channels
 *   trigger an asynchronous reconnection attempt through {@link ActiveTransceiver} to return to the active pool.
 */
public final class NChannelPool implements CompletionHandler<Void, Transceiver.Peer>
{
	private final BlockingDeque<Transceiver.Peer> connected = new LinkedBlockingDeque<>();
	private ActiveTransceiver endpoint;

	/**
	 * Sets up the channel pool.
	 *
	 * @param addrList the list of socket addresses
	 */
	public void setup(List<SocketAddress> addrList) {
		endpoint = new ActiveTransceiver(addrList);
		endpoint.boot(this);
	}

	/**
	 * Called when a connection is completed.
	 *
	 * @param result the result
	 * @param peer the peer
	 */
	@Override
	public void completed(Void result, Transceiver.Peer peer) {
		NChannelPool.this.connected.addLast(peer);
	}

	/**
	 * Called when a connection fails.
	 *
	 * @param exc the exception
	 * @param peer the peer
	 */
	@Override
	public void failed(Throwable exc, Transceiver.Peer peer) {
		endpoint.broken(peer);
		//endpoint.waitAndReconnect(peer.addr(), 1000, this);
	}

	/**
	 * Leases a channel from the pool.
	 *
	 * @param timeout the timeout
	 * @param unit the time unit
	 * @return the peer
	 * @throws InterruptedException if interrupted
	 */
	public Transceiver.Peer leaseChannel(long timeout, TimeUnit unit) throws InterruptedException {
		return this.connected.pollFirst(timeout, unit);
	}

	/**
	 * Releases a channel back to the pool.
	 *
	 * @param peer the peer
	 */
	public void releaseChannel(Transceiver.Peer peer) {
		connected.addLast(peer);
	}

	/**
	 * Trashes a channel.
	 *
	 * @param peer the peer
	 */
	public void trashChannel(Transceiver.Peer peer) {
		endpoint.broken(peer);

		SocketAddress addr = peer.addr();
		//endpoint.waitAndReconnect(addr, 1000, this);
	}
}
