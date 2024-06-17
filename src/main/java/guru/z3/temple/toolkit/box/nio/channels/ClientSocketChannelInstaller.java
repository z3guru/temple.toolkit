package guru.z3.temple.toolkit.box.nio.channels;

import guru.z3.temple.toolkit.box.ItemInstaller;
import guru.z3.temple.toolkit.box.ItemOperator;
import guru.z3.temple.toolkit.box.ModuleConfiguration;
import guru.z3.temple.toolkit.box.io.SocketConfigurationHelper;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSocketChannelInstaller implements ItemInstaller<Channel>
{
	private ModuleConfiguration conf;
	final private AtomicBoolean aborted = new AtomicBoolean(false);

	private ClientSocketChannelInstaller()
	{

	}

	static ClientSocketChannelInstaller of(ModuleConfiguration conf)
	{
		ClientSocketChannelInstaller installer = new ClientSocketChannelInstaller();
		installer.conf = conf;

		return installer;
	}

	@Override
	public void install(ItemOperator<Channel> os) throws IOException
	{
		SocketAddress remote = SocketConfigurationHelper.getSocketAddress(conf);
		SocketChannel ch = SocketChannel.open();
		Selector selector = Selector.open();

		ch.configureBlocking(true);
		ch.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_CONNECT);
		ch.connect(remote);

		//
		long waitConnectionTimeout = 10000L; // TODO byte settings...
		long stTime = System.currentTimeMillis();
		long waiting = 0L;

		while ( !this.aborted.get() && waiting < waitConnectionTimeout )
		{
			int count = selector.select(waitConnectionTimeout);
			if ( this.aborted.get() ) throw new IOException("[E-XXXXX] Aborted");

			if ( count > 0 )
			{
				Set<SelectionKey> keys = selector.selectedKeys();
				for ( SelectionKey k : keys )
				{
					keys.remove(k);
					if ( k.isConnectable() )
					{
						ch.finishConnect();
						//
						if ( !this.aborted.get() ) os.installed(ch);
						return;
					}
				}
			}

			waiting = System.currentTimeMillis() - stTime;
			if ( waiting < 10 ) break;
		}

		throw new IOException("[E-XXXXX] Connection Timeout");
	}

	@Override
	public void abort()
	{
		this.aborted.set(true);
		Thread.currentThread().interrupt();
	}
}
