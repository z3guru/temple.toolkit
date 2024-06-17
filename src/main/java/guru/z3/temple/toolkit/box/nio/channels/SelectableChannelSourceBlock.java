package guru.z3.temple.toolkit.box.nio.channels;

import guru.z3.temple.toolkit.box.io.IOBlock;

import java.io.IOException;
import java.nio.channels.Channel;

public class SelectableChannelSourceBlock<T> implements ChannelHandler, IOBlock<T>
{
	@Override
	public void attach(Channel ch) throws IOException
	{

	}

	@Override
	public void link(IOBlock<T> peer)
	{

	}

	@Override
	public IOBlock<T> unlink()
	{
		return null;
	}

	@Override
	public Boolean apply(IOBlock ioBlock, T data)
	{
		return null;
	}
}
