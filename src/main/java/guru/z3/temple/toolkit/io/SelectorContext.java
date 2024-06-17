package guru.z3.temple.toolkit.io;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.function.Consumer;

public class SelectorContext
{
	private Selector selector;

	private SelectorContext() throws IOException
	{
		this.selector = Selector.open();
	}

	public void open()
	{
		
	}

	public void close() throws IOException
	{
		this.selector.close();
	}

	public void select(long timeout) throws IOException
	{
		if( this.selector.select(timeout) > 0 )
		{
			Set<SelectionKey> keys = this.selector.selectedKeys();
			keys.forEach(k->callAttachment(k));
			keys.clear();
		}
	}

	public void callAttachment(SelectionKey k)
	{
		if( k.attachment() == null ) return;

		try
		{
			Consumer<SelectionKey> consumer = (Consumer<SelectionKey>)k.attachment();
			consumer.accept(k);
		}
		catch(Exception e)
		{

		}
	}
}
