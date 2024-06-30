package guru.z3.temple.toolkit.knot

fun<T> KnottingTool.getKnot(name: String, block: () -> T): T
{
	val v: T = block()
	crafting(name, v)

	return v
}
