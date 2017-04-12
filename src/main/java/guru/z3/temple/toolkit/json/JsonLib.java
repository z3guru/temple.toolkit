package guru.z3.temple.toolkit.json;

import java.io.*;

/**
 * JSON 라이브러리는 여러가지가 있다. 필요에 따라 라이브러리가 변경될 수 있다
 * 그럴 때 최소한 함수 호출과 관련되서 소스변경이 없도록 한다
 */
public interface JsonLib
{
	/**
	 * 특정 오브젝트를 JSON문자열로 변환한다.
	 *
	 * @param obj 대상 오프젝트
	 * @return 변환된 JSON문자열
	 * @throws IOException
	 */
	public String serialize(Object obj) throws IOException;

	/**
	 * 특정 오브젝트를 JSON 문자열로 변환후 {@link OutputStream}으로 출력한다
	 *
	 * @param obj 대상 오프젝트
	 * @param output 출력지점
	 * @return 변환된 JSON문자열
	 * @throws IOException
	 */
	public void serialize(Object obj, OutputStream output) throws IOException;

	/**
	 * 특정 오브젝트를 JSON문자열로 변환후 {@link Writer}로 출력한다.
	 *
	 * @param obj 대상 오프젝트
	 * @param writer 출력지점
	 * @return 변환된 JSON문자열
	 * @throws IOException
	 */
	public void serialize(Object obj, Writer writer) throws IOException;

	/**
	 * 입력된 JSON 문자열로부터 오브젝트를 추출해낸다
	 *
	 * @param json 입력 JSON 문자열
	 * @param type 오브젝트 타입
	 * @return 추출된 오브젝트
	 * @throws IOException
	 */
	public <T> T deserialize(String json, Class<T> type) throws IOException;

	/**
	 * 입력된 JSON 문자열로부터 오브젝트를 추출해낸다
	 *
	 * @param input 입력지점
	 * @param type 오브젝트 타입
	 * @return 추출된 오브젝트
	 * @throws IOException
	 */
	public <T> T deserialize(InputStream input, Class<T> type) throws IOException;

	/**
	 * 입력된 JSON 문자열로부터 오브젝트를 추출해낸다
	 *
	 * @param reader 입력지점
	 * @param type 오브젝트 타입
	 * @return 추출된 오브젝트
	 * @throws IOException
	 */
	public <T> T deserialize(Reader reader, Class<T> type) throws IOException;
}
