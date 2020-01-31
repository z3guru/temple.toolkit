package unittest.temple.toolkit.json;

import guru.z3.temple.toolkit.json.JsonLib;
import guru.z3.temple.toolkit.json.JsonTool;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by jaeda on 2017. 1. 31..
 */
public class TestJsonLib
{
	@Test
	public void testBasic() throws Exception
	{
		ParentVO p = new ParentVO();
		ChildVO c = new ChildVO();
		c.getFruits().add("apple");
		c.getFruits().add("banna");
		p.setNum(100);
		p.setChild(c);

		JsonLib jlib = JsonTool.defaultLib();
		String json = jlib.serialize(p);
		System.out.println(json);

		ParentVO p2 = jlib.deserialize(json, ParentVO.class);

		TestCase.assertEquals(p.getNum(), p2.getNum());
		TestCase.assertEquals(p.getChild().getFruits().get(1), p2.getChild().getFruits().get(1));
	}

	/*
	private class ParentVO
	{
		private int num;
		private String ss;
		private ChildVO child;

		private transient int num2;

		public int getNum() { return num; }
		public void setNum(int num) { this.num = num; }

		public String getSs() { return ss; }
		public void setSs(String ss) { this.ss = ss; }

		public ChildVO getChild() { return child; }
		public void setChild(ChildVO child) { this.child = child; }

		@Transient
		public int getNum2() { return num2; }
		public void setNum2(int num2) { this.num2 = num2; }
	}

	private class ChildVO
	{
		private List<String> fruits;

		public ChildVO()
		{
			this.fruits = new LinkedList<String>();
		}

		public List<String> getFruits() { return fruits; }
		public void setFruits(List<String> fruits) { this.fruits = fruits; }
	}
	*/
}
