package guru.z3.temple.toolkit.box;

public interface Module extends Handler
{
	State getState();
	void ready(ModuleConfiguration conf) throws RuntimeException;
}
