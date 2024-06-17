package guru.z3.temple.toolkit.box.nio.channels;

import guru.z3.temple.toolkit.box.ModuleConfiguration;

import java.io.IOException;

public interface ChannelFactory
{
	void assign(ModuleConfiguration conf, Class<ChannelHandler> klassHandler) throws IOException;

	void abort();
}
