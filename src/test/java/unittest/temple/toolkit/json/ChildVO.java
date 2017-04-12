package unittest.temple.toolkit.json;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jaeda on 2017. 3. 6..
 */
public class ChildVO
{
	private List<String> fruits;

	public ChildVO()
	{
		this.fruits = new LinkedList<String>();
	}

	public List<String> getFruits() { return fruits; }
	public void setFruits(List<String> fruits) { this.fruits = fruits; }
}
