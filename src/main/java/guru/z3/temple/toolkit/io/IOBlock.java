package guru.z3.temple.toolkit.io;

import java.io.IOException;

public interface IOBlock<T>
{
	IOBlock<T> send(T buf) throws IOException;
	IOBlock<T> recv(T buf) throws IOException;

	interface Parser<T>
	{
		void parse(T data) throws IOException;
	}

	interface Builder<T,D>
	{
		T build() throws IOException;
	}
}
