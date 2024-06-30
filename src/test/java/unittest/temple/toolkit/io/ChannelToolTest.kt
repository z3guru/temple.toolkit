package unittest.temple.toolkit.io

import guru.z3.temple.toolkit.io.ChannelTool
import guru.z3.temple.toolkit.io.ServerChannelTool
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.TimeUnit

class ChannelToolTest
{
	@Test
	fun test1()
	{
		println("test1")
	}

	@Test
	fun testOpen(): Unit = runBlocking {
		println("Step 1")

		val svrTool = ServerChannelTool()
		val svrProcess = launch {
			svrTool.listen(InetSocketAddress(9876)) {
				println("[SVR] accepted")
			}

			println("svr is listening")
		}

		// delay some
		delay(100)

		var isConnected = false
		val addr = InetSocketAddress("127.0.0.1", 9876)

		val clntTool = ChannelTool.connect(addr, 5, TimeUnit.SECONDS)

		delay(100)
		svrTool.close()
		svrProcess.cancelAndJoin()
		//Assertions.assertTrue(isConnected)
	}

	@Test
	fun testOpen3(): Unit = runBlocking {
		println("Step 1")

		val svrTool = ServerChannelTool()
		val svrProcess = launch {
			svrTool.listen(InetSocketAddress(9876)) {
				println("[SVR] accepted")
			}

			println("svr is listening")
		}

		// delay some
		delay(60 * 60 * 1000L)

		svrTool.close()
		svrProcess.cancelAndJoin()
		//Assertions.assertTrue(isConnected)
	}


	object MyCompleteHandler : CompletionHandler<AsynchronousSocketChannel, Nothing?>
	{
		override fun completed(result: AsynchronousSocketChannel?, attachment: Nothing?)
		{
			println("accept completed")
		}

		override fun failed(exc: Throwable?, attachment: Nothing?)
		{
			println("accept failed")
		}

	}
}