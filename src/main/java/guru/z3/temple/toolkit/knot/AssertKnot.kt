package guru.z3.temple.toolkit.knot

fun<T> KnottingTool.assertKnot(expected: T, actual:() -> T): Boolean
{
	return expected == actual()
}