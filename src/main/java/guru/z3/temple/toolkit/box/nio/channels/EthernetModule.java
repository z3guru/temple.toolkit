package guru.z3.temple.toolkit.box.nio.channels;

import guru.z3.temple.toolkit.ToolKit;
import guru.z3.temple.toolkit.box.*;
import guru.z3.temple.toolkit.concurrent.JobRunnable;
import guru.z3.temple.toolkit.concurrent.Worker;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EthernetModule implements Module
{
	private ChannelFactory chFactory;
	private Class<ChannelHandler> chHandlerKlass;

	final private AtomicReference<Worker> plumber = new AtomicReference<>();
	final private AtomicBoolean connected = new AtomicBoolean(false);

	@Override
	public void start() throws RuntimeException
	{
		this.plumber.set(ToolKit.defaultWorkerPool().execute(null, new ConnectionPlumber()));
	}

	@Override
	public void stop(boolean force) throws RuntimeException
	{
		if ( this.plumber.get() != null )
		{
			Worker worker = this.plumber.getAndSet(null);
			worker.stop(force);
		}
	}

	@Override
	public State getState()
	{
		return null;
	}

	@Override
	public void ready(ModuleConfiguration conf) throws RuntimeException
	{
		// TODO check Type... & provides variants...

		// new instance of Application ...
		Application app = null;

		Box.getInstance().putApplication(app.getName(), app);

		//
		this.chFactory = null;
		this.chHandlerKlass = null;
	}

	private void onConnected()
	{
		this.connected.set(true);
	}

	private class ConnectionPlumber implements JobRunnable
	{
		@Override
		public void setup() throws RejectedExecutionException
		{
		}

		@Override
		public boolean working()
		{
			try
			{
				EthernetModule.this.chFactory.assign(null, null);
				EthernetModule.this.onConnected();
				return false;
			}
			catch(Exception e)
			{
			}

			return true;
		}

		@Override
		public int stopped(boolean abort)
		{
			return 0;
		}
	}
}
