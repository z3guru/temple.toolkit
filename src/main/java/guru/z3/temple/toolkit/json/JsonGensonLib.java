package guru.z3.temple.toolkit.json;


import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.ext.GensonBundle;

import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Genson customize
 */
public class JsonGensonLib extends GensonBundle implements JsonLib
{
	/** singleton instance */
	private final static JsonGensonLib instance = new JsonGensonLib();
	/** 기본 {@link Genson} */
	private Genson genson;

	@Override
	public void configure(GensonBuilder gb)
	{
		gb.useDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.acceptSingleValueAsList(true)
				.useIndentation(true);
	}

	private JsonGensonLib()
	{
		this.genson = new GensonBuilder().withBundle(this).create();
	}

	public static JsonLib getLib()
	{
		return instance;
	}

	@Override
	public String serialize(Object obj) throws IOException
	{
		return this.genson.serialize(obj);
	}

	@Override
	public void serialize(Object obj, OutputStream output) throws IOException
	{
		this.genson.serialize(obj, output);
	}

	@Override
	public void serialize(Object obj, Writer writer) throws IOException
	{
		this.genson.serialize(obj, writer);
	}

	@Override
	public <T> T deserialize(String json, Class<T> type) throws IOException
	{
		return this.genson.deserialize(json, type);
	}

	@Override
	public <T> T deserialize(InputStream input, Class<T> type) throws IOException
	{
		return this.genson.deserialize(input, type);
	}

	@Override
	public <T> T deserialize(Reader reader, Class<T> type) throws IOException
	{
		return this.genson.deserialize(reader, type);
	}
}
