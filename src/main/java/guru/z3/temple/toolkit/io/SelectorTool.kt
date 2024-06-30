package guru.z3.temple.toolkit.io

import java.io.IOException
import java.nio.channels.SelectableChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector

object SelectorTool
{
	private val selector = Selector.open()

	@Throws(IOException::class, InterruptedException::class)
	fun close()
	{
		this.selector.close()
	}

	@Throws(IOException::class)
	fun register(ch: SelectableChannel, ops: Int, f: (SelectionKey)->Unit)
	{
		ch.register(this.selector, ops, f)
	}

	@Throws(IOException::class)
	fun select(timeout: Long)
	{
		this.selector.select({
			val f: (SelectionKey) -> Unit = it.attachment() as (SelectionKey) -> Unit
			f.invoke(it)
		}, timeout)
	}
}