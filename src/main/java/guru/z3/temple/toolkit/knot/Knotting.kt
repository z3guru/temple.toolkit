package guru.z3.temple.toolkit.knot

import guru.z3.temple.toolkit.io.ByteStreamReader

fun Knotting(craft: Craft?, workshop: Workshop = Workshop, doing: KnottingTool.() -> Unit): Craft
{
	val tool: KnottingTool = KnottingToolImpl(workshop.reader)

	tool.doing()

	return Craft()
}

class KnottingToolImpl(override val reader: ByteStreamReader) : KnottingTool
{
	override fun getTool(): KnottingTool = this
	override fun <T> crafting(name: String, value: T?)
	{
		TODO("Not yet implemented")
	}
}