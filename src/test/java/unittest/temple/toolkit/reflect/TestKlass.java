/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package unittest.temple.toolkit.reflect;

import guru.z3.temple.toolkit.reflect.Klass;
import org.junit.Test;

import java.util.List;

public class TestKlass
{
	@Test
	public void testClassNames() throws Exception
	{
		ClassLoader loader = getClass().getClassLoader();
		List<String> names = Klass.classNames(loader, "org.apache.logging.log4j.core", false);
		//List<String> names = Klass.classNames(loader, "guru.z3.temple.toolkit", false);

		for ( String n : names )
		{
			System.out.println(n);
		}
	}
}
