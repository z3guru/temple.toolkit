package guru.z3.temple.toolkit.io

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.channels.*
import java.util.concurrent.Phaser
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 *
 * @param channel
 * @param buf
 */
class ByteStreamReader private constructor(val channel: ReadableByteChannel, val buf:ByteBuffer)
{
	private val logger: Logger = LogManager.getContext().getLogger(ByteStreamReader::class.java.name)

	/**   */
	var timeout: Long = 0
	private val phaser = Phaser()
	private var readPhase = 0

	companion object {
		fun of(channel: ReadableByteChannel, buf:ByteBuffer): ByteStreamReader
		{
			return ByteStreamReader(channel, buf)
		}
	}

	/**
	 * 1바이트를 읽는다.
	 */
	@Throws(IOException::class, BufferUnderflowException::class)
	fun readByte(): Byte {
		if (!buf.hasRemaining()) readMore(this.timeout)
		return buf.get()
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readBytes(bytes: ByteArray) {
		waitReadable(bytes.size, this.timeout)
		buf[bytes]
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readBytes(bytes: ByteArray, offset: Int, len: Int) {
		waitReadable(len, this.timeout)
		buf[bytes, offset, len]
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readBuf(destBuf: ByteBuffer, len: Int) {
		waitReadable(len, this.timeout)
		destBuf.put(this.buf)
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readShort(): Short
	{
		waitReadable(java.lang.Short.BYTES, this.timeout)
		return buf.getShort()
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readInt(): Int
	{
		waitReadable(Integer.BYTES, this.timeout)
		return buf.getInt()
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readLong(): Long
	{
		waitReadable(java.lang.Long.BYTES, this.timeout)
		return buf.getLong()
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readFloat(): Float
	{
		waitReadable(java.lang.Float.BYTES, this.timeout)
		return buf.getFloat()
	}

	@Throws(IOException::class, BufferUnderflowException::class)
	fun readDouble(): Double
	{
		waitReadable(java.lang.Double.BYTES, this.timeout)
		return buf.getDouble()
	}

	@Throws(IOException::class)
	private fun open(temple: SelectorTool)
	{
		temple.register(this.channel as SelectableChannel, SelectionKey.OP_READ) {
			this.phaser.arrive()
		}
	}

	@Throws(IOException::class)
	fun close()
	{
		this.phaser.forceTermination()
	}

	/**
	 * 지정된 timeout시간안에 count수 바이트만큼 데이터를 읽을 때까지 기다린다.
	 * @param count 읽고자 하는 바이트 수
	 * @param timeout
	 * @throws IOException
	 */
	@Throws(IOException::class, TimeoutException::class)
	fun waitReadable(count: Int, timeout: Long)
	{
		if (buf.remaining() >= count) return

		val tm = System.currentTimeMillis()
		var ellapse = timeout
		do {
			readMore(timeout)
			if (buf.remaining() >= count) return;

			ellapse = System.currentTimeMillis() - tm
		} while (ellapse < (timeout - 5))

		throw TimeoutException()
	}

	@Throws(IOException::class, TimeoutException::class)
	private fun readMore(timeout: Long): Int {

		try
		{
			val newPhase = this.phaser.awaitAdvanceInterruptibly(this.readPhase, timeout, TimeUnit.MILLISECONDS)
			if (newPhase < 0) throw IOException("phaser is terminated")

			this.readPhase = newPhase
			return this.channel.read(this.buf)
		}
		catch(ex: TimeoutException)
		{
			throw ex
		}
	}
}