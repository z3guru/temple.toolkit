package guru.z3.toolkit.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * The AsyncDatagramServer class.
 */
public class AsyncDatagramServer
{
	/**
	 * Opens the server.
	 *
	 * @param port the port
	 * @param clientHandler the client message handler
	 * @throws IOException if an I/O error occurs
	 */
	public void open(int port, ClientMessageHandler clientHandler) throws IOException {
		if (clientHandler == null) throw new IOException("clientHandler is null");

		try (Selector selector = Selector.open(); DatagramChannel channel = DatagramChannel.open()) {
			// setup Non-blocking mode
			channel.configureBlocking(false);
			channel.bind(new InetSocketAddress(port));

			channel.register(selector, SelectionKey.OP_READ);
			ByteBuffer buffer = ByteBuffer.allocate(2048);

			while (channel.isOpen()) {
				// 이벤트가 발생하기를 기다림 (블로킹)
				selector.select();

				// 준비된 키(이벤트) 집합을 가져옴
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					keyIterator.remove(); // 처리된 키는 제거

					if (key.isReadable()) {
						buffer.clear();
						handleRead(key, buffer, clientHandler); // 데이터 수신 처리
					}
				}
			}
		}
	}

	/**
	 * Handles a read event.
	 *
	 * @param key the selection key
	 * @param buffer the byte buffer
	 * @param clientHandler the client message handler
	 * @throws IOException if an I/O error occurs
	 */
	private void handleRead(SelectionKey key, ByteBuffer buffer, ClientMessageHandler clientHandler) throws IOException {
		DatagramChannel serverCh = (DatagramChannel) key.channel();
		SocketAddress clientAddr = serverCh.receive(buffer);
		clientHandler.handleMessage(clientAddr, buffer);
	}

	/**
	 * The ClientMessageHandler interface.
	 */
	public interface ClientMessageHandler {
		/**
		 * Handles a message.
		 *
		 * @param clientAddress the client address
		 * @param message the message
		 */
		void handleMessage(SocketAddress clientAddress, ByteBuffer message);
	}
}
