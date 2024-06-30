package guru.z3.temple.toolkit.knot

import java.nio.ByteBuffer

fun KnottingTool.bufferingKnot(name: String, length: Int)
{
	reader.waitReadable(length, 1000)
	val buf: ByteBuffer = ByteBuffer.allocate(length)
	reader.readBuf(buf, length)
	crafting(name, buf)
}
