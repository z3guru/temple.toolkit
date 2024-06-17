package guru.z3.temple.toolkit.box.io;

import guru.z3.temple.toolkit.box.AbortError;
import guru.z3.temple.toolkit.box.ModuleConfiguration;
import guru.z3.temple.toolkit.box.State;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.TimeoutException;

abstract public class SelectableChannelBlock extends SourceBlock<ByteBuffer>
{
	private String remoteIp;
	private int remotePort;
	private ByteChannel ch;
	private Selector selector;

	private ByteBuffer readBuf;

	@Override
	public State getState()
	{
		return null;
	}

	@Override
	public void ready(ModuleConfiguration conf) throws RuntimeException
	{
		try { this.ch = openChannel(); }
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	protected void iterateWork() throws IOException, AbortError
	{
	}

	@Override
	protected int kill(boolean force)
	{
		return 0;
	}

	abstract ByteChannel openChannel();
	abstract void onConnect() throws IOException;

	void onRead() throws IOException, TimeoutException
	{
		while ( this.ch.read(this.readBuf) > 0 )
		{
			this.readBuf.flip();
			flow(this.readBuf.duplicate());
			this.readBuf.compact();

			if ( !this.readBuf.hasRemaining() )
			{
				// buffer is full.
				throw new IOException("readbuf is full");
			}
		}
	}

	private void handleSelector() throws IOException, TimeoutException
	{
		int kcount = this.selector.select(5000 /*TODO by setting*/);

		if ( kcount > 0 )
		{
			Set<SelectionKey> keys = this.selector.selectedKeys();
			for ( SelectionKey k : keys )
			{
				handleSelectionKey(k);
				keys.remove(k);
			}
		}
	}

	private void handleSelectionKey(SelectionKey k) throws IOException, TimeoutException
	{
		if ( k.isReadable() ) onRead();
		else if ( k.isConnectable() ) onConnect();
	}
}
