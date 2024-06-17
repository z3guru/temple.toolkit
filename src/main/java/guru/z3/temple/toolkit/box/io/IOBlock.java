package guru.z3.temple.toolkit.box.io;

import java.util.function.BiFunction;

/**
 * TODO add feature to share state
 * @param <T>
 */
public interface IOBlock<T> extends BiFunction<IOBlock, T, Boolean>
{
	/**
	 *
	 * @param peer
	 * @return peer
	 */
	void link(IOBlock<T> peer);

	IOBlock<T> unlink();
}
