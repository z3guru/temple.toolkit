package guru.z3.temple.toolkit.box;

public interface Handler
{
	void start() throws RuntimeException;
	void stop(boolean force) throws RuntimeException;
}
