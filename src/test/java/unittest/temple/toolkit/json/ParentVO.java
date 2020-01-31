package unittest.temple.toolkit.json;

import java.beans.Transient;

/**
 * Created by jaeda on 2017. 3. 6..
 */
public class ParentVO
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
