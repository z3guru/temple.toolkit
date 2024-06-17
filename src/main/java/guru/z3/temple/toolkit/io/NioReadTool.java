package guru.z3.temple.toolkit.io;

import guru.z3.temple.toolkit.concurrent.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * 기존의 {@link ByteBuffer}는 NIO를 처리할 때 까다로운 점이 있다
 * ...
 */
public class NioReadTool
{
	private final Logger logger = LogManager.getContext().getLogger(NioReadTool.class.getName());

	/** 통신채널인데 {@link Selector}를 지원해야 한다 */
	private ReadableByteChannel channel;

	/** channel에서 데이터를 읽어서 공급해주는 녀석 */
	private Worker worker;
	/** 비동기 처리시 이벤트수신기, SELECT */
	private Selector selector;

	/** 통신버퍼 */
	private ByteBuffer buf;
	/** 통신버퍼 */
	private long timeout;

	private NioReadTool()
	{
	}

	public static NioReadTool link(ReadableByteChannel channel) throws Exception
	{
		if ( ! (channel instanceof SelectableChannel) ) throw new IllegalArgumentException("channel must be SelectableChannel");

		NioReadTool nb = new NioReadTool();
		nb.open(channel);

		return nb;
	}

	/**
	 * 1바이트를 읽는다.
	 */
	public byte get() throws IOException, BufferUnderflowException
	{
		if ( !this.buf.hasRemaining() ) readMore(this.timeout);
		return this.buf.get();
	}

	public void get(byte[] bytes) throws IOException, BufferUnderflowException
	{
		waitReadable(bytes.length, this.timeout);
		this.buf.get(bytes);
	}

	public void get(byte[] bytes, int offset, int len) throws IOException, BufferUnderflowException
	{
		waitReadable(len, this.timeout);
		this.buf.get(bytes, offset, len);
	}

	public short getShort() throws IOException, BufferUnderflowException
	{
		waitReadable(Short.BYTES, this.timeout);
		return this.buf.getShort();
	}

	public int getInt() throws IOException, BufferUnderflowException
	{
		waitReadable(Integer.BYTES, this.timeout);
		return this.buf.getInt();
	}

	public long getLong() throws IOException, BufferUnderflowException
	{
		waitReadable(Long.BYTES, this.timeout);
		return this.buf.getLong();
	}

	public float getFloat() throws IOException, BufferUnderflowException
	{
		waitReadable(Float.BYTES, this.timeout);
		return this.buf.getFloat();
	}

	public double getDouble() throws IOException, BufferUnderflowException
	{
		waitReadable(Double.BYTES, this.timeout);
		return this.buf.getDouble();
	}

	/**
	 * 지정된 timeout시간안에 count수 바이트만큼 데이터를 읽을 때까지 기다린다.
	 * @param count 읽고자 하는 바이트 수
	 * @param timeout
	 * @throws IOException
	 */
	public void waitReadable(int count, long timeout) throws IOException
	{
		if ( this.buf.remaining() >= count ) return;

		long tm = System.currentTimeMillis();
		long ellapse = timeout;
		do
		{
			readMore(timeout);
			ellapse = System.currentTimeMillis() - tm;

		} while ( this.buf.remaining() < count && ellapse < timeout );

		throw new InterruptedByTimeoutException();
	}

	private void open(ReadableByteChannel channel) throws IOException
	{
		try
		{
			// setup base ========================
			this.channel = channel;

			int sz = 1024;
			try { sz = Integer.parseInt(System.getProperty("toolkit.nio.readbuf.sz", "1024")); } catch(Exception e) { }
			this.buf = ByteBuffer.allocate(sz);
			this.buf.flip();

			// setup selector ====================
			SelectableChannel ch = (SelectableChannel)this.channel;
			ch.configureBlocking(false);

			this.selector = Selector.open();
			ch.register(this.selector, SelectionKey.OP_READ);
			if ( logger.isTraceEnabled() ) logger.trace("NioReadTool[ch={}] opened", ch);
		}
		catch(Exception e)
		{
			try { this.selector.close(); } catch(Exception ex) { } finally { this.selector = null; }
			throw new IOException("Error in opening NioReadTool:" + e.getMessage(), e);
		}
	}


	private int readMore(long timeout) throws IOException
	{
		int count = 0;

		try
		{
			int kcount = this.selector.select(timeout);

			if ( kcount > 0 )
			{
				Set<SelectionKey> keys = this.selector.selectedKeys();
				for ( SelectionKey key : keys )
				{
					if ( key.isReadable() )
					{
						this.buf.compact();
						count = this.channel.read(this.buf);
						this.buf.flip();
					}
					keys.remove(key);

					// 데이터를 읽었거나 통신오류가 있다면... 반복문을 빠져 나간다.
					if ( count != 0 ) break;
				}
			}
			else
			{
				count = this.channel.read(buf);
			}

			// 통신오류이면...
			if ( count == -1 ) throw new IOException("Recv count == -1, channel is closed by peer");
		}

		catch(IOException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new IOException(e.getMessage(), e);
		}

		return count;
	}

	public void close() throws IOException
	{
		try { this.selector.close(); }
		catch(Exception e) { throw new IOException("Error in closing NioReadTool:" + e.getMessage(), e); }
		finally
		{
			this.selector = null;
			if ( logger.isTraceEnabled() ) logger.trace("NioReadTool closed");
		}
	}

	// GETTER/SETTER methods ///////////////////////////////

	public long getTimeout() { return timeout; }
	public void setTimeout(long timeout) { this.timeout = timeout; }
}
