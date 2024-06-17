package guru.z3.temple.toolkit.box;

import java.io.IOException;

abstract public class RunnableModule implements Module, Runnable
{
	private boolean stopped;

	@Override
	public void start() throws RuntimeException
	{

	}

	@Override
	public void stop(boolean force) throws RuntimeException
	{
		this.stopped = true;
		Thread.currentThread().interrupt();
	}

	@Override
	public void run()
	{
		while(!this.stopped)
		{
			try
			{
				iterateWork();
			}
			catch(AbortError error)
			{

			}
			catch(Exception e)
			{

			}

			//
			interval();
		}
	}

	protected void interval()
	{
		try { Thread.sleep(1000); } catch(InterruptedException e) { }
	}

	abstract protected void iterateWork() throws IOException, AbortError;
	abstract protected int kill(boolean force);
}
