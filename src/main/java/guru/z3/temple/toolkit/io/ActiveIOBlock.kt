package guru.z3.temple.toolkit.io

import guru.z3.temple.toolkit.box.io.SocketConfigurationHelper
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.Channel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.*

class ActiveIOBlock : IOBlock<ByteBuffer>
{
	lateinit var ch: SocketChannel

	fun connect(ip:String, port:Int): IOBlock<ByteBuffer>?
	{
		this.ch = SocketChannel.open()

		val remote = SocketConfigurationHelper.getSocketAddress(ip, port)
		val selector = Selector.open()

		this.ch.configureBlocking(true)
		this.ch.register(selector, SelectionKey.OP_CONNECT)
		this.ch.connect(remote)

		if( selector.select(5000) > 0 )
		{
			val keys = selector.keys()
			val iter = keys.iterator()

			if( keys.stream().anyMatch { it.isConnectable } )
			{
				this.ch.finishConnect();

				//
				//selector.
				keys.clear()
				this.ch.register(selector, )

				return this;
			}
		}

		return null;
	}

	fun read(request: ByteBuffer, parser: (ByteBuffer)->Any)
	{

	}
}