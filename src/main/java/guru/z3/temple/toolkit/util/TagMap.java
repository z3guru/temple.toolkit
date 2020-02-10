package guru.z3.temple.toolkit.util;

import java.util.Collection;

public interface TagMap
{
	/**
	 * Get a value from map.
	 *
	 * @param name
	 * @return value with the name, if error then null
	 */
	Tag get(String name);

	/**
	 * Put a value by name. if there is one with the name of 1st parameter, that is replaced by 2nd parameter.
	 *
	 * @param name
	 * @param tag new tag
	 * @return the previous value associated with name, or null if there was none.
	 */
	Object put(String name, Tag tag);

	/**
	 * Reset data of tags in this map for reworking...
	 */
	void reset();

	/**
	 * return all tags
	 * @return
	 */
	Collection<Tag> allTags();
}
