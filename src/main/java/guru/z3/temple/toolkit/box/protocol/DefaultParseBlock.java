package guru.z3.temple.toolkit.box.protocol;

import guru.z3.temple.toolkit.box.State;
import guru.z3.temple.toolkit.box.io.SinkBlock;
import guru.z3.temple.toolkit.box.io.SourceBlock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultParseBlock implements SinkBlock<ByteBuffer>
{
	final private Exchanger<ByteBuffer> bufExchanger = new Exchanger<>();

	/** Network inter-character timeout */
	private long t8;

	@Override
	public void sink(SourceBlock source, ByteBuffer data) throws IOException, TimeoutException
	{
		try { this.bufExchanger.exchange(data, t8, TimeUnit.MILLISECONDS); }
		catch(InterruptedException e) { }
	}

	@Override
	public void sourceStateChanged(State s)
	{

	}

	private void readMore() throws TimeoutException
	{
		try
		{
			ByteBuffer buf = this.bufExchanger.exchange(null, t8, TimeUnit.MILLISECONDS);
		}
		catch(InterruptedException e)
		{
		}
	}
}
