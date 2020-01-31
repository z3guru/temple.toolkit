package guru.z3.temple.toolkit;

import guru.z3.temple.toolkit.concurrent.WorkerPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ToolKit
{
	private final static ToolKit instance = new ToolKit();
	private final Logger logger = LogManager.getContext().getLogger(WorkerPool.class.getName());

	/** 기본으로 제공하는 {@link WorkerPool} */
	private WorkerPool workerPool;

	private ToolKit()
	{
		// 기본 WorkerPool을 생성한다.
		// 환경변수에서 필요한 정보를 읽어온다.
		int coreSz = 20;
		try { coreSz = Integer.parseInt(System.getProperty("toolkit.thread.pool.core", "20")); } catch(Exception e) { }
		int maxSz = 100;
		try { maxSz = Integer.parseInt(System.getProperty("toolkit.thread.pool.max", "100")); } catch(Exception e) { }
		int queueSz = 10;
		try { queueSz = Integer.parseInt(System.getProperty("toolkit.thread.pool.queue", "10")); } catch(Exception e) { }

		long keepAliveTime = 60L;
		try { keepAliveTime = Long.parseLong(System.getProperty("toolkit.thread.pool.keepAliveTime", "60")); } catch(Exception e) { }

		//
		if ( logger.isTraceEnabled() )
			logger.trace("toolkit.thread.pool.core={}, max={}, queue={}, keepAliveTime={}sec", coreSz, maxSz, queueSz, keepAliveTime);

		// 쓰레드풀 생성
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSz);
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(coreSz, maxSz, keepAliveTime, TimeUnit.SECONDS, queue);
		this.workerPool = new WorkerPool(tpe);
	}

	public static ToolKit getInstance()
	{
		return instance;
	}

	public static WorkerPool defaultWorkerPool()
	{
		return instance.workerPool;
	}
}
