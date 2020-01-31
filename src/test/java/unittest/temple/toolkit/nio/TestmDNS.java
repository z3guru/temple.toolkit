/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package unittest.temple.toolkit.nio;

import guru.z3.temple.toolkit.nio.ByteBufferUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Created by jaeda on 2017. 11. 13..
 */
public class TestmDNS
{
	private final static String MDNS_IP		= "224.0.0.251";
	private final static int MDNS_PORT		= 5353;

	@Test
	public void testQuery() throws Exception
	{
		NetworkInterface ni = NetworkInterface.getByName("en9");
		DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)
				.setOption(StandardSocketOptions.SO_REUSEADDR, true)
				.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);

		dc.connect(new InetSocketAddress(MDNS_IP, MDNS_PORT));
		ByteBuffer buf = ByteBuffer.allocate(1024);

		while(true)
		{
			System.out.println("Reading...");
			buf.clear();
			int count = dc.read(buf);
			buf.flip();
			System.out.println(ByteBufferUtils.serialize(buf));
		}

	}

}
