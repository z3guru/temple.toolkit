package guru.z3.temple.toolkit.util;

import java.util.function.Function;

public enum TagType
{
	Byte(Byte.class, Tag.PARSE_BYTE)
	, Short(Short.class, Tag.PARSE_SHORT)
	, Integer(Integer.class, Tag.PARSE_INT)
	, Long(Long.class, Tag.PARSE_LONG)
	, Float(Float.class, Tag.PARSE_FLOAT)
	, Double(Double.class, Tag.PARSE_DOUBLE)
	, String(String.class, Tag.PARSE_STRING);

	/** this attribute represents type */
	protected Class klass;
	/** a function for parsing */
	protected Function parseFunc;

	TagType(Class klass, Function parseFunc)
	{
		this.klass = klass;
		this.parseFunc = parseFunc;
	}

	public Class<? extends Number> klass()
	{
		return this.klass;
	}

	public Function parseFunc()
	{
		return this.parseFunc;
	}

	static public TagType valueOf(Class klass) throws IllegalArgumentException
	{
		try { return valueOf(klass.getSimpleName()); }
		catch(Exception e)
		{
			throw new IllegalArgumentException("Unsupported class:" + klass.getName());
		}
	}
}
