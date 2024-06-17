package guru.z3.temple.toolkit.io;

import java.nio.ByteBuffer;
import java.util.concurrent.Exchanger;

public class ByteBufferReadTool
{
	final private Exchanger<ByteBuffer> bufExchanger = new Exchanger<>();

	/** Network inter-character timeout */
	private long t8;
	/** buf */
	private ByteBuffer buf;

	public ByteBufferReadTool(int bufSize, long t8)
	{

	}
}
