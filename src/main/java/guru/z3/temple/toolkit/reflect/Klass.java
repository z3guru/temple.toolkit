/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package guru.z3.temple.toolkit.reflect;

import guru.z3.temple.toolkit.nio.NioReadTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;

public class Klass
{
	private final static Klass instance = new Klass();
	private final Logger logger = LogManager.getContext().getLogger(Klass.class.getName());

	private Klass()
	{
	}

	public static List<String> classNames(ClassLoader loader, String pkg) throws IOException
	{
		String[] pkgs = pkg.split(":");
		List<String> list = new LinkedList();

		for ( String pp : pkgs )
		{
			String path = pp.replace('.', '/');
			URL url = loader.getResource(path);
			instance.logger.debug("find classes in a path={}", path);

			if ( "file".equals(url.getProtocol()) )
			{
				instance.classNames(new File(url.getFile()), list);
			}
			else if ( "jar".equals(url.getProtocol()) )
			{
				instance.classNames((JarURLConnection)url.openConnection(), path, list);
			}
		}

		return list;
	}

	private void classNames(JarURLConnection conn, String path, List<String> list) throws IOException
	{
		for ( Enumeration<JarEntry> e = conn.getJarFile().entries(); e.hasMoreElements(); )
		{
			JarEntry ee = e.nextElement();
			String name = ee.getName();
			int from = path.length();

			if ( name.startsWith(path) && !ee.isDirectory() && name.endsWith(".class") )
			{
				String classPath = name.substring(0, name.length() - 6/* .class */);
				list.add(classPath.replace('/', '.'));
			}
		}
	}

	private void classNames(File pkgDir, List<String> list)
	{
		for ( File f : pkgDir.listFiles() )
		{
			String name = f.getName();

			if ( name.endsWith(".class") && f.canRead() )
			{
				String classPath = name.substring(0, name.length() - 6/* .class */);
				list.add(classPath.replace('/', '.'));
			}
			else if ( f.isDirectory() ) classNames(f, list);
		}
	}
}
