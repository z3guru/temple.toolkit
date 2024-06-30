package guru.z3.temple.toolkit.io

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.*
import java.util.concurrent.TimeUnit

enum class ChannelState
{
	None, Disconnected, Connected
}

class ChannelTool private constructor(private val channel: AsynchronousSocketChannel)
{
	companion object {
		fun accepted(channel: AsynchronousSocketChannel): ChannelTool
		{
			return ChannelTool(channel)
		}

		fun connect(remote: InetSocketAddress, timeout: Long, toUnit: TimeUnit): ChannelTool
		{
			var channel: AsynchronousSocketChannel? = null
			try {
				channel = AsynchronousSocketChannel.open()
				val f = channel!!.connect(remote)
				f.get(timeout, toUnit)

				return ChannelTool(channel!!)
			}
			finally
			{
				channel?.close()
			}
		}
	}

	fun read(buf: ByteBuffer, timeout: Long, toUnit: TimeUnit): Int
	{
		val f = this.channel.read(buf)
		return f.get(timeout, toUnit)
	}

	fun write(buf: ByteBuffer, timeout: Long, toUnit: TimeUnit): Int
	{
		val f = this.channel.write(buf)
		return f.get(timeout, toUnit)
	}
}