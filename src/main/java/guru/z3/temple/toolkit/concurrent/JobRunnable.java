package guru.z3.temple.toolkit.concurrent;

import java.util.concurrent.RejectedExecutionException;

/**
 * A agent {@link Worker} do ...
 *
 */
public interface JobRunnable
{
	public final static int NOERROR	= 0;

	/**
	 * 이 인터페이스 구현 객체는 {@link WorkerPool#execute}함수를 통해 쓰레드상에서 동작하게 된다
	 * 쓰레드가 할당되서 동작이 시작될 때 처음 한번 호출된다.
	 */
	public void setup() throws RejectedExecutionException;
	
	/**
	 * {@link Worker}쓰레드에서 반복적으로 호출된다.
	 * @return 계속 진행여부 만약 false가 반환되면 작업을 종료한다.
	 */
	public boolean working();
	
	/**
	 * {@link #working()}함수가 false를 반환하면 {@link Worker} 쓰레드는 작업을 종료하는데 종료전 한번
	 * 이 함수를 호출해준다.
	 *
	 * @param abort 외부에 의해서 강제로 종료되면 true, 아니면 false
	 * @return error code, 0이면 정상으로 처리한다.
	 */
	public int stopped(boolean abort);
}
