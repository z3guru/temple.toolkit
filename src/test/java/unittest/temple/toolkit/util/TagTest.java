package unittest.temple.toolkit.util;

import guru.z3.temple.toolkit.util.Tag;
import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigInteger;

public class TagTest
{
	@Test
	public void getValue()
	{
		Tag tag = Tag.newTag("NUM1", String.class, "123");

		int actual = tag.getNumber(Integer.class, null).intValue();
		TestCase.assertEquals(123, actual);
	}

	@Test
	public void getMismatchedType()
	{
		Tag tag = Tag.newTag("NUM1", Byte.class, (byte)250);
		TestCase.assertEquals(250, tag.intValue());
		TestCase.assertEquals(250f, tag.floatValue());
		TestCase.assertEquals(250d, tag.doubleValue());

		tag = Tag.newTag("NUM1", Short.class, (short)0x8FF0);
		TestCase.assertEquals(0x8FF0, tag.intValue());

		tag = Tag.newTag("NUM1", Integer.class, 0x8FF01234);
		TestCase.assertEquals(0x8FF01234L, tag.longValue());
	}

	@Test
	public void stringType()
	{
		Tag tag = Tag.newTag("NUM1", String.class, "220.23");
		//TestCase.assertEquals(220, tag.shortValue());
		//TestCase.assertEquals(220, tag.intValue());
		TestCase.assertEquals(220.23f, tag.floatValue());
		TestCase.assertEquals(220.23d, tag.doubleValue());
	}


	@Test
	public void getNumberPerformance()
	{
		int count = 1000000;

		Tag tag = Tag.newTag("NUM1", String.class, "123");

		long stTime = System.currentTimeMillis();
		for ( int i = 0; i < count; i++ )
		{
			int actual = tag.getNumber(Integer.class, 0).intValue();
		}
		long lapTime = System.currentTimeMillis() - stTime;
		System.out.println("getNumber(String => int) laptime(msec)=" + lapTime);


		Tag<Integer> tag2 = Tag.newTag("NUM2", Integer.class, 123);
		stTime = System.currentTimeMillis();
		for ( int i = 0; i < count; i++ )
		{
			int actual = tag2.getValue();
		}
		lapTime = System.currentTimeMillis() - stTime;
		System.out.println("getNumber(int => int) laptime(msec)=" + lapTime);


		Tag tag3 = Tag.newTag("NUM3", Double.class, 123d);
		stTime = System.currentTimeMillis();
		for ( int i = 0; i < count; i++ )
		{
			int actualD = tag3.intValue();
		}
		lapTime = System.currentTimeMillis() - stTime;
		System.out.println("getNumber(Double => int) laptime(msec)=" + lapTime);


		Tag tag4 = Tag.newTag("NUM4", Long.class, 0x8123456701234567L);
		stTime = System.currentTimeMillis();
		for ( int i = 0; i < count; i++ )
		{
			BigInteger actualD = tag3.unsignedValue();
		}
		lapTime = System.currentTimeMillis() - stTime;
		System.out.println("getNumber(Double => int) laptime(msec)=" + lapTime);

	}
}
