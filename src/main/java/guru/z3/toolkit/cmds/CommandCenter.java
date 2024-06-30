package guru.z3.toolkit.cmds;

import guru.z3.toolkit.nio.AsyncDatagramServer;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The CommandCenter class.
 */
public class CommandCenter implements AsyncDatagramServer.ClientMessageHandler
{
	private final Map<SocketAddress, ClientInfo> clientMap = new ConcurrentHashMap<>();

	/**
	 * Handles a message from a client.
	 *
	 * @param clientAddr the client address
	 * @param buf the message buffer
	 */
	@Override
	public void handleMessage(SocketAddress clientAddr, ByteBuffer buf) {
		if (clientAddr == null) throw new IllegalArgumentException("clientAddr is null");
		if (buf == null) throw new IllegalArgumentException("message is null");
		if (buf.remaining() <= 0) return;

		ClientInfo info = clientMap.computeIfAbsent(clientAddr, ClientInfo::new);

		// Recev
		buf.flip(); // 읽기 모드로 전환
		String message = StandardCharsets.UTF_8.decode(buf).toString().trim();
		System.out.printf("[RECV] %s: %s", clientAddr, message);

		// Send Reply

	}

	/**
	 * The ClientInfo record.
	 *
	 * @param addr the socket address
	 */
	private record ClientInfo(SocketAddress addr) { }
}
