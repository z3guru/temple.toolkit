package guru.z3.toolkit.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link AsynchronousByteChannel}에서 데이터를 읽는 도구입니다.
 */
public class ReadTool implements Closeable
{
	private final System.Logger logger = System.getLogger(ReadTool.class.getName());

	/** 통신채널인데 {@link Selector}를 지원해야 합니다. */
	private AsynchronousByteChannel channel;

	/** 통신버퍼 */
	private ByteBuffer rbuf;
	/** 통신버퍼 */
	private long timeout;

	/**
	 * ReadTool을 생성합니다.
	 */
	private ReadTool()
	{
		this.timeout = 3000L;
	}

	/**
	 * 지정된 채널에 ReadTool을 연결합니다.
	 *
	 * @param channel 연결할 채널
	 * @return 연결된 ReadTool
	 * @throws IllegalArgumentException 채널이 null인 경우
	 */
	public static ReadTool attach(AsynchronousByteChannel channel) throws IllegalArgumentException
	{
		return attach(channel, ByteBuffer.allocate(1024));
	}

	/**
	 * 지정된 채널과 버퍼로 ReadTool을 연결합니다.
	 *
	 * @param channel 연결할 채널
	 * @param buff 사용할 버퍼
	 * @return 연결된 ReadTool
	 * @throws IllegalArgumentException 채널이 null인 경우
	 */
	public static ReadTool attach(AsynchronousByteChannel channel, ByteBuffer buff) throws IllegalArgumentException
	{
		if (channel == null) throw new IllegalArgumentException("channel is null");

		ReadTool nb = new ReadTool();
		nb.channel = channel;
		nb.rbuf = buff;

		// initialize
		nb.rbuf.clear();
		nb.rbuf.limit(0);

		return nb;
	}

	/**
	 * ReadTool을 닫습니다.
	 *
	 * @throws IOException 닫기 중 오류가 발생한 경우
	 */
	@Override
	public void close() throws IOException {
		AsynchronousByteChannel ch = this.channel;
		this.channel = null;
		this.rbuf = null;

		if (ch != null) {
			try { ch.close(); }
			catch (IOException e) { throw e; }
			catch (Exception e) { throw new IOException(e); }
		}
	}

	/**
	 * 1바이트를 읽습니다.
	 *
	 * @return 읽은 바이트
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public byte get() throws IOException, BufferUnderflowException, TimeoutException
	{
		// 타임아웃을 루프마다 리셋하지 않도록 waitReadable을 사용
		waitReadable(1, this.timeout);
		return this.rbuf.get();
	}

	/**
	 * 바이트 배열을 읽습니다.
	 *
	 * @param bytes 읽은 데이터를 저장할 배열
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public void get(byte[] bytes) throws IOException, BufferUnderflowException, TimeoutException {
		get(bytes, 0, bytes.length);
	}

	/**
	 * 바이트 배열의 일부를 읽습니다.
	 *
	 * @param bytes 읽은 데이터를 저장할 배열
	 * @param offset 배열의 시작 오프셋
	 * @param len 읽을 바이트 수
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public void get(byte[] bytes, int offset, int len) throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(len, this.timeout);
		this.rbuf.get(bytes, offset, len);
	}

	/**
	 * ByteBuffer를 읽습니다.
	 *
	 * @param buf 읽은 데이터를 저장할 버퍼
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public void get(ByteBuffer buf) throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(buf.remaining(), this.timeout);
		buf.put(buf.position(), this.rbuf, this.rbuf.position(), buf.remaining());
	}

	/**
	 * 버퍼의 일부를 가져옵니다.
	 *
	 * @param count 가져올 바이트 수
	 * @return 버퍼의 일부
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public ByteBuffer getSliceBuffer(int count) throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(count, this.timeout);

		var sliced = this.rbuf.slice();
		this.rbuf.position(this.rbuf.position() + count);
		sliced.limit(this.rbuf.position());

		return sliced;
	}

	/**
	 * short 값을 읽습니다.
	 *
	 * @return 읽은 short 값
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public short getShort() throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(Short.BYTES, this.timeout);
		return this.rbuf.getShort();
	}

	/**
	 * int 값을 읽습니다.
	 *
	 * @return 읽은 int 값
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public int getInt() throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(Integer.BYTES, this.timeout);
		return this.rbuf.getInt();
	}

	/**
	 * long 값을 읽습니다.
	 *
	 * @return 읽은 long 값
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public long getLong() throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(Long.BYTES, this.timeout);
		return this.rbuf.getLong();
	}

	/**
	 * float 값을 읽습니다.
	 *
	 * @return 읽은 float 값
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public float getFloat() throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(Float.BYTES, this.timeout);
		return this.rbuf.getFloat();
	}

	/**
	 * double 값을 읽습니다.
	 *
	 * @return 읽은 double 값
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws BufferUnderflowException 버퍼에 데이터가 없는 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	public double getDouble() throws IOException, BufferUnderflowException, TimeoutException {
		waitReadable(Double.BYTES, this.timeout);
		return this.rbuf.getDouble();
	}

	/**
	 * 지정된 timeout시간안에 count수 바이트만큼 데이터를 읽을 때까지 기다립니다.
	 *
	 * @param count 읽고자 하는 바이트 수
	 * @param timeout 타임아웃
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	private void waitReadable(int count, long timeout) throws IOException, TimeoutException {
		if (this.rbuf.remaining() >= count) return;

		// 전체 타임아웃을 deadline 기반으로 정확히 소모
		final long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeout);
		do
		{
			long remains = deadline - System.nanoTime();
			if (remains <= 0L) throw new TimeoutException();

			long toRead = TimeUnit.NANOSECONDS.toMillis(Math.max(1000L, remains));
			readMore(toRead);
			if ( this.rbuf.remaining() >= count ) return;

		} while (true);
	}

	/**
	 * 더 많은 데이터를 읽습니다.
	 *
	 * @param timeout 타임아웃
	 * @return 읽은 바이트 수
	 * @throws IOException 읽기 중 오류가 발생한 경우
	 * @throws TimeoutException 타임아웃이 발생한 경우
	 */
	private int readMore(long timeout) throws IOException, TimeoutException
	{
		try
		{
			this.rbuf.compact();
			var future = this.channel.read(this.rbuf);
			return future.get(timeout, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException _) {  Thread.currentThread().interrupt(); }
		catch (TimeoutException e) { throw e; }
		catch(Exception e)
		{
			throw new IOException(e.getMessage(), e);
		}
		finally {
			// 어떤 경우에도 읽기 모드로 복귀
			this.rbuf.flip();
		}

		return 0;
	}


	// GETTER/SETTER methods ///////////////////////////////

	/**
	 * 타임아웃을 가져옵니다.
	 *
	 * @return 타임아웃
	 */
	public long getTimeout() { return timeout; }

	/**
	 * 타임아웃을 설정합니다.
	 *
	 * @param timeout 설정할 타임아웃
	 */
	public void setTimeout(long timeout) { this.timeout = timeout; }
}
