package guru.z3.temple.toolkit.box;

import java.util.HashMap;
import java.util.Map;

public class ModuleAdapter
{
	final private Map<String,Module> moduleMap = new HashMap<>();

	public Module attachModule(String name, Module module)
	{
		return this.moduleMap.put(name, module);
	}

	public Module detachModule(String name)
	{
		return this.moduleMap.remove(name);
	}

	public Module findModule(String name)
	{
		return this.moduleMap.get(name);
	}
}
