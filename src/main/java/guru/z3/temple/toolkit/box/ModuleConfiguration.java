package guru.z3.temple.toolkit.box;

public interface ModuleConfiguration
{
	ModuleConfiguration sub(String subCategory);

	Object get(String name);
}
