/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package unittest.temple.toolkit.reflect;

import guru.z3.temple.toolkit.reflect.Klass;
import junit.framework.TestCase;
import org.apache.logging.log4j.core.Core;
import org.junit.Test;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@TestAnnotation
@XmlRootElement
public class TestKlass
{
	@Test
	public void testClassNames() throws Exception
	{
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		List<String> names = Klass.classNames(loader, "org.apache.logging.log4j.core", false);
		TestCase.assertTrue(names.size() < 50);
		TestCase.assertTrue(names.stream().anyMatch(s -> Core.class.getName().equals(s)));
		names = Klass.classNames(loader, "org.apache.logging.log4j.core", true);
		TestCase.assertTrue(names.size() > 50);
	}

	@Test
	public void testFindByAnnotation() throws Exception
	{
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		List<Class> classes = Klass.findByAnnotation(TestAnnotation.class, "unittest");
		TestCase.assertTrue(classes.size() == 1);
		TestCase.assertEquals(TestKlass.class.getName(), classes.get(0).getName());
	}

	@Test
	public void testFindByFilter() throws Exception
	{
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		List<Class> classes = Klass.findByFilter(new Klass.ClassFilter() {
			@Override
			public boolean accept(Class klass) {
				return klass.getName().equals(TestKlass.class.getName());
			}
		}, "unittest");

		TestCase.assertTrue(classes.size() == 1);
		TestCase.assertEquals(TestKlass.class.getName(), classes.get(0).getName());
	}

}
