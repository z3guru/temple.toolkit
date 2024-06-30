package guru.z3.temple.toolkit.io

import kotlinx.coroutines.delay
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.atomic.AtomicBoolean

class ServerChannelTool : CompletionHandler<AsynchronousSocketChannel, (AsynchronousSocketChannel) -> Unit>
{
	private val stopped = AtomicBoolean(false)
	private val accepting = AtomicBoolean(false)

	suspend fun listen(addr: InetSocketAddress, onAccept: (AsynchronousSocketChannel) -> Unit)
	{
		val channel = AsynchronousServerSocketChannel.open()
		channel.bind(addr)

		this.stopped.set(false)
		while (!this.stopped.get())
		{
			if (accepting.compareAndSet(false, true))
			{
				channel.accept(onAccept, this)
			}
			delay(1000)
		}
	}

	fun close()
	{
		this.stopped.set(true)
	}

	override fun completed(result: AsynchronousSocketChannel, attachment: ((AsynchronousSocketChannel) -> Unit))
	{
		attachment(result)
		this.accepting.set(false)
	}

	override fun failed(exc: Throwable?, attachment: ((AsynchronousSocketChannel) -> Unit))
	{
		this.accepting.set(false)
		this.stopped.set(false)
	}
}
