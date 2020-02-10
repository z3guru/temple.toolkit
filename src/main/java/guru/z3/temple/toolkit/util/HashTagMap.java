/*
This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by-nd/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/
package guru.z3.temple.toolkit.util;

import java.util.Collection;
import java.util.HashMap;

public class HashTagMap extends HashMap<String,Tag> implements TagMap
{
	@Override
	public Tag get(String name)
	{
		return get(name);
	}

	@Override
	public void reset()
	{
		allTags().forEach(t -> t.reset());
	}

	@Override
	public Collection<Tag> allTags()
	{
		return values();
	}
}
