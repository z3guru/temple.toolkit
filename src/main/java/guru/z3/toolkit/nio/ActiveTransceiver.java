package guru.z3.toolkit.nio;

import guru.z3.toolkit.log.ToolkitLog;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The ActiveTransceiver class.
 */
public class ActiveTransceiver implements Transceiver
{
	final private Set<SocketAddress> addrSet = new HashSet<>();

	/**
	 * Creates an ActiveTransceiver.
	 *
	 * @param addrList the list of socket addresses
	 */
	public ActiveTransceiver(List<SocketAddress> addrList) {
		this.addrSet.addAll(addrList);
	}

	/**
	 * Boots the transceiver.
	 *
	 * @param handler the completion handler
	 */
	@Override
	public void boot(CompletionHandler<Void, Peer> handler) {
		for (SocketAddress addr : this.addrSet) {
			connect(addr, handler);
		}
	}

	/**
	 * Breaks the connection with the peer.
	 *
	 * @param peer the peer
	 */
	@Override
	public void broken(Peer peer) {
		this.addrSet.remove(peer.addr());

		// if attachment.channel != null then close it
		if (peer.channel() != null) {
			try { peer.channel().close(); } catch (IOException e) { /* TODO logging */ }
		}

		//
		waitAndReconnect(peer.addr(), 0, null);
	}

	/**
	 * Waits and reconnects to the address.
	 *
	 * @param addr the socket address
	 * @param delay the delay
	 * @param handler the completion handler
	 */
	private void waitAndReconnect(SocketAddress addr, long delay, CompletionHandler<Void, Peer> handler) {
		//var delay = NioContext.getInstance().getT5();
		if (this.addrSet.add(addr)) {
			NioContext.getInstance().getScheduler().schedule(() -> connect(addr, handler), delay, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Connects to the address.
	 *
	 * @param addr the socket address
	 * @param handler the completion handler
	 */
	private void connect(SocketAddress addr, CompletionHandler<Void, Peer> handler) {
		try {
			var ch = AsynchronousSocketChannel.open();
			ToolkitLog.logger().log(System.Logger.Level.DEBUG, "@Toolkit Connecting to {0}", addr);

			ch.connect(addr, new Peer(addr, ch), handler);
		}
		catch (IOException e) {
			ToolkitLog.logger().log(System.Logger.Level.ERROR, "@Toolkit Failed to connect to {0}", addr);
			handler.failed(e, new Peer(addr, null));
		}
	}
}
