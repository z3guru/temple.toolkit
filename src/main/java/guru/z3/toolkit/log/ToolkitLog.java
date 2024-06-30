package guru.z3.toolkit.log;

/**
 * The ToolkitLog class.
 */
public class ToolkitLog
{
	private static final ToolkitLog instance = new ToolkitLog();

	final System.Logger logger = System.getLogger(ToolkitLog.class.getName());

	private ToolkitLog() {

	}

	/**
	 * Logger system.
	 *
	 * @return the system
	 */
	public static System.Logger logger() { return instance.logger; }

}
