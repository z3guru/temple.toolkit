package guru.z3.temple.toolkit.util;

public class NonNumberTag<V> extends Tag<V>
{
	public NonNumberTag(String name, Class<V> valueClass)
	{
		super(name, valueClass);
	}

	@Override
	public Number getNumber()
	{
		return getNumber(TagType.Integer, 0);
	}

	@Override
	public <T extends Number> T getNumber(TagType type, T defaultValue)
	{
		Object value = getValue();
		if ( value == null ) return defaultValue;

		return (T)type.parseFunc().apply(String.valueOf(value));
	}
}
