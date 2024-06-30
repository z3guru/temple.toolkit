package guru.z3.temple.toolkit.knot

import guru.z3.temple.toolkit.io.ByteStreamReader

interface KnottingTool
{
	val reader: ByteStreamReader

	fun getTool(): KnottingTool

	fun<T> crafting(name: String, value: T?)
}
