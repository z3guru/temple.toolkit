package guru.z3.toolkit.nio;

import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * The Transceiver interface.
 */
public interface Transceiver
{
	/**
	 * Boots the transceiver.
	 *
	 * @param handler the completion handler
	 */
	void boot(CompletionHandler<Void, Peer> handler);

	/**
	 * Breaks the connection with the peer.
	 *
	 * @param peer the peer
	 */
	void broken(Peer peer);

	/**
	 * The Peer record.
	 *
	 * @param addr the socket address
	 * @param channel the asynchronous socket channel
	 */
	record Peer(SocketAddress addr, AsynchronousSocketChannel channel) { }
}
