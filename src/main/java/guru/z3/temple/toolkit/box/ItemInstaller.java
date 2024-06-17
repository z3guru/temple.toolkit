package guru.z3.temple.toolkit.box;

import java.io.IOException;

public interface ItemInstaller<T>
{
	void install(ItemOperator<T> os) throws IOException;

	void abort();
}
