package guru.z3.temple.toolkit.box.io;

import guru.z3.temple.toolkit.box.RunnableModule;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

abstract public class SourceBlock<T> extends RunnableModule
{
	private SinkBlock<T> peer;

	public void link(SinkBlock<T> peer)
	{
		this.peer = peer;
	}

	public SinkBlock<T> unlink()
	{
		return this.peer;
	}

	public void flow(T data) throws IOException, TimeoutException
	{
		if ( this.peer != null ) this.peer.sink(this, data);
	}
}
