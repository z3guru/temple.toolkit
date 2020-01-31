package unittest.temple.toolkit.nio;

import guru.z3.temple.toolkit.ToolKit;
import guru.z3.temple.toolkit.concurrent.JobRunnable;
import guru.z3.temple.toolkit.concurrent.Worker;
import guru.z3.temple.toolkit.nio.NioReadTool;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by jaeda on 2017. 2. 6..
 */
public class TestNioReadBuffer
{
	private final Logger logger = LoggerFactory.getLogger(NioReadTool.class.getName());

	private Thread parentThread;
	private NioReadTool rb;


	@Test
	public void testGet() throws Exception
	{
		Pipe pipe = Pipe.open();

		Worker peer = ToolKit.defaultWorkerPool().execute("peer", new TestPeer(pipe.sink()));
		Pipe.SourceChannel source = pipe.source();
		source.configureBlocking(false);

		this.rb = NioReadTool.link(source);
		byte[] buf = new byte[100];

		rb.get();
		rb.get();
		try { Thread.sleep(1000); } catch(InterruptedException e) { }
		peer.stop(false);

		parentThread = Thread.currentThread();
		for ( int i = 0; i < 3; i++ )
		{
			try { rb.get(buf); }
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try { Thread.sleep(1000); } catch(InterruptedException e) { }
		}

		try { Thread.sleep(3000); } catch(InterruptedException e) { }
	}

	private class TestPeer implements JobRunnable
	{
		private WritableByteChannel channel;

		public TestPeer(WritableByteChannel channel)
		{
			this.channel = channel;
		}

		@Override
		public void setup() throws RejectedExecutionException
		{

		}

		@Override
		public boolean working()
		{
			try
			{
				this.channel.write(ByteBuffer.wrap(new byte[] { 0x01 }));
				try { Thread.sleep(500); } catch(InterruptedException e) { }
			}
			catch(Exception e)
			{
			}

			return true;
		}

		@Override
		public int stopped(boolean abort)
		{
			//try { Thread.sleep(1000); } catch(InterruptedException e) { }
			try { rb.close(); }
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				System.out.println("interrupt parent's thread");

				this.channel.write(ByteBuffer.wrap(new byte[] { 0x02 }));
				try { Thread.sleep(1000); } catch(InterruptedException e) { }
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			return 0;
		}
	}
}
