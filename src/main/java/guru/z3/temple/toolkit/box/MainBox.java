package guru.z3.temple.toolkit.box;

import java.util.Map;

abstract public class MainBox extends RunnableModule
{
	private ModuleConfiguration conf;
	private Map<String,Module> moduleMap;

	final private ModuleAdapter adapter = new ModuleAdapter();

	@Override
	public void run()
	{
	}

	abstract public ModuleConfiguration loadBoxConfiguration();

	public ModuleAdapter moduleAdapter()
	{
		return this.adapter;
	}
}
