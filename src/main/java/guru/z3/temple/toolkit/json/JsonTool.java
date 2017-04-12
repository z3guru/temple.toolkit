package guru.z3.temple.toolkit.json;

/**
 * JSON과 관련된 기능을 지원한다.
 */
public class JsonTool
{
	/**
	 * JSON 라이브러리를 감싸고 있는 {@link JsonLib}를 구해준다.
	 * @return
	 */
	public static JsonLib defaultLib()
	{
		return JsonGensonLib.getLib();
	}
}
