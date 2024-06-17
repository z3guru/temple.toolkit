package guru.z3.temple.toolkit.box.io;

import guru.z3.temple.toolkit.box.ModuleConfiguration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketConfigurationHelper
{
	static public SocketAddress getSocketAddress(ModuleConfiguration conf)
	{
		return null;
	}

	static public SocketAddress getSocketAddress(String ip, int port)
	{
		return InetSocketAddress.createUnresolved(ip, port);
	}
}
