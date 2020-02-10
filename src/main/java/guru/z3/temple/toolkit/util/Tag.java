/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package guru.z3.temple.toolkit.util;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * This handles a value with a name.
 * This has fixed type for value.
 *
 * @param <V>
 */
abstract public class Tag<V>
{
	static final public Function<String, Byte> 	    PARSE_BYTE 		= (s) -> Byte.parseByte(s);
	static final public Function<String, Short> 	PARSE_SHORT		= (s) -> Short.parseShort(s);
	static final public Function<String, Integer> 	PARSE_INT 		= (s) -> Integer.parseInt(s);
	static final public Function<String, Long> 		PARSE_LONG		= (s) -> Long.parseLong(s);
	static final public Function<String, Float>  	PARSE_FLOAT		= (s) -> Float.parseFloat(s);
	static final public Function<String, Double>  	PARSE_DOUBLE	= (s) -> Double.parseDouble(s);
	static final public Function<Object, String>  	PARSE_STRING	= (s) -> String.valueOf(s);

	protected String name;
	protected AtomicReference<V> valueRef;

	/** default value, when this tag is reset */
	protected V initValue;

	protected TagType tagType;
	protected boolean readonly;

	public Tag(String name, Class<V> valueClass)
	{
		this.name = name;
		this.valueRef = new AtomicReference<>();
		this.tagType = TagType.valueOf(valueClass);
	}

	public static <T> Tag<T> newTag(String name, Class<T> klass) throws IllegalArgumentException
	{
		if ( klass == null ) throw new IllegalArgumentException("Class must be defined");
		if ( Number.class.isAssignableFrom(klass) ) return new NumberTag(name, klass);
		else
			return new NonNumberTag(name, klass);
	}

	public static <T> Tag<T> newTag(String name, Class<T> klass, T initValue) throws IllegalArgumentException
	{
		Tag<T> tag = newTag(name, klass);
		tag.setValue(initValue);
		return tag;
	}

	/**
	 * Get a value
	 * @return
	 */
	public V getValue()
	{
		return this.valueRef.get();
	}

	/**
	 * Put a value with {@link Tag}.
	 *
	 * @param newValue new value
	 * @return the previous value associated with name, or null if there was none.
	 */
	public V setValue(Object newValue)
	{
		if ( this.readonly ) throw new RuntimeException("This tag is read-only");

		V value = null;

		if ( newValue != null )
		{
			if ( isSameType(newValue.getClass()) ) value = (V)newValue;
			else
				value = (V)this.tagType.parseFunc().apply(newValue);
		}

		return this.valueRef.getAndSet(value);
	}

	/**
	 * set the value with {@link #initValue}
	 */
	public void reset()
	{
		setValue(this.initValue);
	}

	public boolean isSameType(Class klass)
	{
		return this.tagType.klass().isAssignableFrom(klass);
	}

	public boolean isSameType(TagType tagType)
	{
		return this.tagType == tagType;
	}

	public boolean isNumber()
	{
		return Number.class.isAssignableFrom(this.tagType.klass());
	}

	abstract public Number getNumber();

	public byte byteValue()
	{
		return getNumber(TagType.Byte, (byte)0);
	}

	public short shortValue()
	{
		return getNumber(TagType.Short, (short)0);
	}

	public int intValue()
	{
		return getNumber(TagType.Integer, 0);
	}

	public long longValue()
	{
		return getNumber(TagType.Long, 0L);
	}

	public float floatValue()
	{
		return getNumber(TagType.Float, 0f);
	}

	public double doubleValue()
	{
		return getNumber(TagType.Double, 0d);
	}

	public BigInteger unsignedValue()
	{
		if ( this.tagType == TagType.Long )
			return new BigInteger(Long.toUnsignedString(longValue()));
		else
			return BigInteger.valueOf(longValue());
	}

	public Number getNumber(Number defaultValue)
	{
		Number number = getNumber();
		return number == null ? defaultValue : number;
	}

	abstract public <T extends Number> T getNumber(TagType type, T defaultValue);

	public <T extends Number> T getNumber(Class<T> klass, T defaultValue)
	{
		return getNumber(TagType.valueOf(klass), defaultValue);
	}


	// GETTER/SETTER methods =====================
	public String getName() { return name; }

	public TagType getTagType() { return tagType; }

	public AtomicReference<V> getValueRef() { return valueRef; }

	public boolean isReadonly() { return readonly; }
}
