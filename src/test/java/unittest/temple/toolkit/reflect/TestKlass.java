/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package unittest.temple.toolkit.reflect;

import guru.z3.temple.toolkit.reflect.Klass;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@TestAnnotation
public class TestKlass
{
	@Test
	public void testClassNames() throws Exception
	{
		/*
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		List<String> names = Klass.classNames(loader, "org.apache.logging.log4j.core", false);
		TestCase.assertTrue(names.size() < 50);
		TestCase.assertTrue(names.stream().anyMatch(s -> Core.class.getName().equals(s)));
		names = Klass.classNames(loader, "org.apache.logging.log4j.core", true);
		TestCase.assertTrue(names.size() > 50);
		 */
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

	@Test
	public void testTemp() throws Exception
	{
		Pattern p = Pattern.compile("[\\{,]\"([\\w\\.]+)\":.+?\\\\\"V\\\\\":\\\\\"(.+?)\\\\\",");

						   // {"TAG.TAG1":"{\"T\":\"NUMBER\",\"V\":\"123.32\",\"UT\":\"2018-10-10 20:21:22\"}" ..."
		String  testStr1 = "{\"TAG.TAG1\":\"{\\\"T\\\":\\\"NUMBER\\\",\\\"V\\\":\\\"123.32\\\",\\\"UT\\\":\\\"2018-10-10 20:21:22\\\"\"}\""
				         + ",\"TAG.TAG2\":\"{\\\"T\\\":\\\"NUMBER\\\",\\\"V\\\":\\\"123.32\\\",\\\"UT\\\":\\\"2018-10-10 20:21:22\\\"\"}\""
						 + ",\"TAG.TAG3\":\"{\\\"T\\\":\\\"NUMBER\\\",\\\"V\\\":\\\"123.32\\\",\\\"UT\\\":\\\"2018-10-10 20:21:22\\\"\"}\"";

		Matcher m = p.matcher(testStr1);
		int pos = 0;
		while ( m.find(pos) )
		{
			System.out.println(m.group() + "==>" + m.group(1) + ":" + m.group(2));
			pos = m.end();
		}
	}


	@Test
	public void testTemp2() throws Exception
	{
		Pattern p = Pattern.compile("[\\{,]\"([\\w\\.]+)\":.+?\"V\":\"(.+?)\",");

		// {"TAG.TAG1":{"T":"NUMBER","V":"123.32","UT":"2018-10-10 20:21:22"} ..."
		String  testStr1 = "{\"TAG.TAG1\":{\"T\":\"NUMBER\",\"V\":\"123.32\",\"UT\":\"2018-10-10 20:21:22\"}"
				+ ",\"TAG.TAG2\":{\"T\":\"NUMBER\",\"V\":\"123.32\",\"UT\":\"2018-10-10 20:21:22\""
				+ ",\"TAG.TAG3\":{\"T\":\"NUMBER\",\"V\":\"123.32\",\"UT\":\"2018-10-10 20:21:22\"";

		Matcher m = p.matcher(testStr1);
		int pos = 0;
		while ( m.find(pos) )
		{
			System.out.println(m.group() + "==>" + m.group(1) + ":" + m.group(2));
			pos = m.end();
		}
	}

	@Test
	public void testTemp3() throws Exception
	{
		Pattern p = Pattern.compile("[\\{,]\"([\\w\\.]+)\":.+?\"V\":(\"?.+?\"?),");

		// {"TAG.TAG1":{"T":"NUMBER","V":123.32,"UT":"2018-10-10 20:21:22"} ..."
		String  testStr1 = "{\"TAG.TAG1\":{\"T\":\"NUMBER\",\"V\":123.32,\"UT\":\"2018-10-10 20:21:22\"}"
				+ ",\"TAG.TAG2\":{\"T\":\"NUMBER\",\"V\":123.32,\"UT\":\"2018-10-10 20:21:22\""
				+ ",\"TAG.TAG3\":{\"T\":\"NUMBER\",\"V\":\"123.32\",\"UT\":\"2018-10-10 20:21:22\"";

		Matcher m = p.matcher(testStr1);
		int pos = 0;
		while ( m.find(pos) )
		{
			System.out.println(m.group() + "==>" + m.group(1) + ":" + m.group(2));
			pos = m.end();
		}
	}

}
