package guru.z3.temple.toolkit.box.nio.channels;

import java.io.IOException;
import java.nio.channels.Channel;

public interface ChannelHandler
{
	void attach(Channel ch) throws IOException;
}
