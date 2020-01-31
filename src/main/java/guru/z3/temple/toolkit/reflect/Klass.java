/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package guru.z3.temple.toolkit.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;

public class Klass
{
	private final static Klass instance = new Klass();
	private final Logger logger = LoggerFactory.getLogger(Klass.class.getName());

	private Klass()
	{
	}

	public static List<Class> findByAnnotation(Class<? extends Annotation> annotation, String pkgNames) throws IOException
	{
		return findByAnnotation(annotation, ClassLoader.getSystemClassLoader(), pkgNames, true);
	}

	public static List<Class> findByAnnotation(
			Class<? extends Annotation> annotation
			, ClassLoader loader
			, String pkgNames
			, boolean isRecursive) throws IOException
	{
		List<String> classNames = classNames(loader, pkgNames, isRecursive);
		List<Class> classes = new LinkedList();

		for ( String name : classNames )
		{
			try
			{
				Class klass = Class.forName(name);
				//Object[] b1 = klass.getDeclaredAnnotations();
				if ( klass.getAnnotation(annotation) != null ) classes.add(klass);
			}
			catch(ClassNotFoundException e)
			{
			}
		}

		return classes;
	}

	public static List<Class> findByFilter(ClassFilter filter, String pkgNames) throws IOException
	{
		return findByFilter(filter, ClassLoader.getSystemClassLoader(), pkgNames, true);
	}

	public static List<Class> findByFilter(ClassFilter filter, ClassLoader loader, String pkgNames, boolean isRecursive) throws IOException
	{
		List<String> classNames = classNames(loader, pkgNames, isRecursive);
		List<Class> classes = new LinkedList();

		for ( String name : classNames )
		{
			try
			{
				Class klass = Class.forName(name);
				if ( filter.accept(klass) ) classes.add(klass);
			}
			catch(ClassNotFoundException e)
			{
			}
		}

		return classes;
	}

	/**
	 *
	 *
	 * @param loader
	 * @param pkgNames
	 * @param isRecursive wether this contains sub packages or not
	 * @return
	 * @throws IOException
	 */
	public static List<String> classNames(ClassLoader loader, String pkgNames, boolean isRecursive) throws IOException
	{
		String[] pkgs = pkgNames.split(":");
		List<String> list = new LinkedList();

		for ( String pp : pkgs )
		{
			String path = pp.replace('.', '/');
			URL url = loader.getResource(path);
			instance.logger.debug("find classes in a path={}", path);

			if ( "file".equals(url.getProtocol()) )
			{
				instance.classNames(new File(url.getFile()), pp + '.', list, isRecursive);
			}
			else if ( "jar".equals(url.getProtocol()) )
			{
				instance.classNames((JarURLConnection)url.openConnection(), path, list, isRecursive);
			}
		}

		return list;
	}

	private void classNames(JarURLConnection conn, String path, List<String> list, boolean isRecursive) throws IOException
	{
		for ( Enumeration<JarEntry> e = conn.getJarFile().entries(); e.hasMoreElements(); )
		{
			JarEntry ee = e.nextElement();
			String name = ee.getName();
			int recurCheck = path.length();

			if ( name.startsWith(path)
					&& !ee.isDirectory()
					&& name.endsWith(".class") )
			{
				String classPath = name.substring(0, name.length() - 6/* .class */);

				if ( isRecursive || (classPath.lastIndexOf('/') == recurCheck))
				{
					list.add(classPath.replace('/', '.'));
				}
			}
		}
	}

	private void classNames(File pkgDir, String prefix, List<String> list, boolean isRecursive)
	{
		for ( File f : pkgDir.listFiles() )
		{
			String name = f.getName();

			if ( name.endsWith(".class") && f.canRead() )
			{
				String className = name.substring(0, name.length() - 6/* .class */);
				list.add(prefix + className);
			}
			else if ( isRecursive && f.isDirectory() ) classNames(f, prefix + name + '.', list, isRecursive);
		}
	}

	//
	public interface ClassFilter
	{
		public boolean accept(Class klass);
	}
}
