package unittest.temple.toolkit.knot

import guru.z3.temple.toolkit.knot.Knotting
import guru.z3.temple.toolkit.knot.Workshop
import org.junit.jupiter.api.Test

fun Workshop.then()
{
	println("Who am I:" + this.javaClass.name)
}

class KnottingTest
{
	@Test
	fun testOpen()
	{
		println("Step 1")
		Knotting(null) {

			println("Who am I:" + this.javaClass.name)
			//workshop.then()
			//AssertKnot(0xD000, )

		}
	}
}