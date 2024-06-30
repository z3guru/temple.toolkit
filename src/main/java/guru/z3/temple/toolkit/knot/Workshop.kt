package guru.z3.temple.toolkit.knot

import guru.z3.temple.toolkit.io.ByteStreamReader

interface Workshop
{
	val reader: ByteStreamReader
		get() = reader

	companion object: Workshop
	{

	}
}
