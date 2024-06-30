package guru.z3.toolkit.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * NioContext 클래스.
 */
public class NioContext
{
	static private final NioContext instance = new NioContext();

	final private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
	final private ExecutorService es = Executors.newCachedThreadPool();
	private long t5;

	/**
	 * NioContext를 생성합니다.
	 */
	private NioContext() {

	}

	/**
	 * NioContext의 인스턴스를 가져옵니다.
	 *
	 * @return NioContext의 인스턴스
	 */
	public static NioContext getInstance() {
		return instance;
	}

	/**
	 * Runnable을 실행합니다.
	 *
	 * @param r 실행할 Runnable
	 */
	public void execute(Runnable r) { es.execute(r); }

	// GETTER/SETTER =============================

	/**
	 * 스케줄러를 가져옵니다.
	 *
	 * @return 스케줄러
	 */
	public ScheduledExecutorService getScheduler() { return scheduler; }

	/**
	 * T5를 가져옵니다.
	 *
	 * @return T5
	 */
	public long getT5() { return t5; }

	/**
	 * T5를 설정합니다.
	 *
	 * @param t5 설정할 T5
	 */
	public void setT5(long t5) { this.t5 = t5; }
}
