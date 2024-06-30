package guru.z3.toolkit.nio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Asynchronous TCP server that accepts client connections.
 */
public class PassiveTransceiver implements Transceiver, Closeable {

	private final System.Logger logger = System.getLogger(PassiveTransceiver.class.getName());

	private final AsynchronousServerSocketChannel serverSocketChannel;

	/**
	 * Creates an asynchronous server.
	 *
	 * @param port              The port to listen on.
	 * @throws IOException If an I/O error occurs.
	 */
	public PassiveTransceiver(int port) throws IOException {
		this.serverSocketChannel = AsynchronousServerSocketChannel.open();
		this.serverSocketChannel.bind(new InetSocketAddress(port));
	}

	/**
	 * Boots the transceiver.
	 *
	 * @param handler the completion handler
	 */
	@Override
	public void boot(CompletionHandler<Void, Peer> handler) {
		serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
			@Override
			public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
				// Immediately start accepting the next connection
				serverSocketChannel.accept(null, this);

				// Handle the new connection in a separate thread
				try { handler.completed(null, new Peer(clientChannel.getRemoteAddress(), clientChannel)); }
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				// Don't stop accepting connections on failure, unless the channel is closed.
				if (serverSocketChannel.isOpen()) {
					logger.log(System.Logger.Level.ERROR, "Failed to accept connection", exc);
					// To prevent a tight loop on persistent errors, you might want to add a small delay here.
					serverSocketChannel.accept(null, this);
				}

				handler.failed(exc, null);
			}
		});
	}

	/**
	 * Breaks the connection with the peer.
	 *
	 * @param peer the peer
	 */
	@Override
	public void broken(Peer peer) {

	}

	/**
	 * Closes the server socket channel.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void close() throws IOException {
		if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
			serverSocketChannel.close();
		}
	}
}
