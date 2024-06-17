package guru.z3.temple.toolkit.box.io;

public class AbstractIOBloack<T> implements IOBlock<T>
{
	private IOBlock<T> peer;

	@Override
	public void link(IOBlock<T> peer)
	{
		this.peer = peer;
	}

	@Override
	public IOBlock<T> unlink()
	{
		return this.peer;
	}

	@Override
	public Boolean apply(IOBlock peer, T data)
	{
		return null;
	}

	public void sendData(T data)
	{
		if ( this.peer != null ) this.peer.apply(this, data);
	}
}
