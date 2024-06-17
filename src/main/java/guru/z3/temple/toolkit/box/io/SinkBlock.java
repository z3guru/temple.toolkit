package guru.z3.temple.toolkit.box.io;

import guru.z3.temple.toolkit.box.State;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface SinkBlock<T>
{
	void sink(SourceBlock source, T data) throws IOException, TimeoutException;

	void sourceStateChanged(State s);
}
