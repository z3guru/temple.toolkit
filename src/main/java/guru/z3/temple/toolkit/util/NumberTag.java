package guru.z3.temple.toolkit.util;

public class NumberTag<V extends Number> extends Tag<V>
{
	public NumberTag(String name, Class<V> valueClass)
	{
		super(name, valueClass);
	}

	@Override
	public Number getNumber()
	{
		return getValue();
	}

	@Override
	public <T extends Number> T getNumber(TagType type, T defaultValue)
	{
		if ( isSameType(type) ) return (T)getNumber(defaultValue);

		Number value = getValue();
		if ( value == null ) return defaultValue;

		Number returnValue = null;
		switch(type)
		{
			case Byte   : returnValue = value.byteValue(); break;
			case Short  : returnValue = shortFromThis(); break;
			case Integer: returnValue = intFromThis(); break;
			case Long   : returnValue = longFromThis(); break;
			case Float  : returnValue = floatFromThis(); break;
			case Double : returnValue = doubleFromThis(); break;
			default:
				throw new RuntimeException("Tag type is not a number");
		}

		return (T)returnValue;
	}

	private short shortFromThis()
	{
		Number number = getValue();

		switch(this.tagType)
		{
			case Byte   : return (short)(0xFFFF & Byte.toUnsignedInt(number.byteValue()));
		}

		return number.shortValue();
	}

	private int intFromThis()
	{
		Number number = getValue();
		int returnValue = number.intValue();

		switch(this.tagType)
		{
			case Byte   : returnValue = Byte.toUnsignedInt(number.byteValue()); break;
			case Short  : returnValue = Short.toUnsignedInt(number.shortValue()); break;
		}

		return returnValue;
	}

	private long longFromThis()
	{
		Number number = getValue();
		long returnValue = number.intValue();

		switch(this.tagType)
		{
			case Byte   : returnValue = Byte.toUnsignedLong(number.byteValue()); break;
			case Short  : returnValue = Short.toUnsignedLong(number.shortValue()); break;
			case Integer: returnValue = Integer.toUnsignedLong(number.intValue()); break;
		}

		return returnValue;
	}

	private float floatFromThis()
	{
		return this.tagType == TagType.Double
				? getValue().floatValue()
				: (float)longValue();
	}

	private double doubleFromThis()
	{
		return this.tagType == TagType.Float
				? getValue().doubleValue()
				: (double)longValue();
	}

}
