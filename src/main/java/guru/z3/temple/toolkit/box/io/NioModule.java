package guru.z3.temple.toolkit.box.io;

import guru.z3.temple.toolkit.box.Module;
import guru.z3.temple.toolkit.box.ModuleConfiguration;
import guru.z3.temple.toolkit.box.State;

public class NioModule implements Module
{
	private SourceBlock source;
	private SinkBlock sink;

	@Override
	public void start() throws RuntimeException
	{

	}

	@Override
	public void stop(boolean force) throws RuntimeException
	{

	}

	@Override
	public State getState()
	{
		return null;
	}

	@Override
	public void ready(ModuleConfiguration conf) throws RuntimeException
	{

	}

	public SourceBlock getSourceBlock()
	{
		return null;
	}

	public SinkBlock getSinkBlock()
	{
		return null;
	}
}
