package guru.z3.temple.toolkit.io;

import java.util.concurrent.*;

public class NioParser<T> extends FutureTask<T>
{
	public NioParser(Callable<T> callable)
	{
		super(callable);
	}
}
