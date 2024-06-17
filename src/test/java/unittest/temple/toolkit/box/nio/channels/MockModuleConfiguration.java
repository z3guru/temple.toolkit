package unittest.temple.toolkit.box.nio.channels;

import guru.z3.temple.toolkit.box.ModuleConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MockModuleConfiguration implements ModuleConfiguration
{
	final private Map<String,Object> confMap = new HashMap<>();

	@Override
	public ModuleConfiguration sub(String subCategory)
	{
		String s = subCategory + ".";
		this.confMap.entrySet().stream()
				.filter(e -> e.getKey().startsWith(s))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		return null;
	}

	@Override
	public Object get(String name)
	{
		return this.confMap.get(name);
	}

	public void put(String name, Object value)
	{
		this.confMap.put(name, value);
	}
}
