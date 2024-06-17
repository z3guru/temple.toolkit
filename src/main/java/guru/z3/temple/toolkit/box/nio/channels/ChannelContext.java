package guru.z3.temple.toolkit.box.nio.channels;

import java.net.SocketAddress;
import java.nio.channels.Channel;

abstract public class ChannelContext
{
	private Channel channel;
	private SocketAddress peerAddress;

	/**
	 * if an errror is occurred, when use channel, call this function
	 */
	abstract void onError();

	// GETTER/SETTER methods ===============================
	public Channel getChannel() { return channel; }
	public void setChannel(Channel channel) { this.channel = channel; }

	public SocketAddress getPeerAddress() { return peerAddress; }
	public void setPeerAddress(SocketAddress peerAddress) { this.peerAddress = peerAddress; }
}
