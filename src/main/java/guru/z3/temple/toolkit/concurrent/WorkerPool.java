package guru.z3.temple.toolkit.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *     {@link ThreadPoolExecutor}를 기반으로 {@link Worker}구현체를 쓰레드에서 동작시킨다
 * </p>
 *
 * <li></li>
 * 
 */
public class WorkerPool
{
	private final Logger logger = LoggerFactory.getLogger(WorkerPool.class.getName());
	
	/** {@link ThreadPoolExecutor} */
	private ThreadPoolExecutor threadPool;
	/** {@link Worker} map */
	private Map<Integer, Worker> workers;

	
	public WorkerPool(ThreadPoolExecutor threadPool)
	{
		this.workers = new ConcurrentHashMap<Integer, Worker>();
		this.threadPool = threadPool;
	}

	/**
	 * {@link JobRunnable}구현객체를 {@link Worker}객체에 올려 쓰레드로 구동한다.
	 * 
	 * @param name 쓰레드이름=작업이름
	 * @param r {@link JobRunnable}구현객체
	 * @return {@link} Worker
	 */
	public Worker execute(String name, JobRunnable r) throws RejectedExecutionException
	{
		return execute(name, r, null);
	}

	/**
	 * {@link JobRunnable}구현객체를 {@link Worker}객체에 올려 쓰레드로 구동한다.
	 * 이때 {@link JobLocal}구현객체를 {@link ThreadLocal}에 할당한다.
	 *
	 * @param name 쓰레드이름=작업이름
	 * @param r {@link JobRunnable}구현객체
	 * @param local
	 *
	 * @return {@link} Worker
	 */
	public Worker execute(String name, JobRunnable r, JobLocal local) throws RejectedExecutionException
	{
		try
		{
			Worker t = new WorkerImpl(name, r, local);
			this.threadPool.execute(t);
			this.workers.put(t.hashCode(), t);
			
			return t;
		}
		catch(RejectedExecutionException e)
		{
			logger.warn("Worker[{}] rejected", name);
			throw e;
		}
	}

	/**
	 * 이름으로 {@link Worker}를 찾는다. 이름이 중복될 수 있으므로, 여러개가 찾아질 수 있다.
	 * 
	 * @param name 쓰레드이름=작업이름
	 * @return List<Worker>
	 */
	public Collection<Worker> findWorker(String name)
	{

		if ( name != null )
		{
			return this.workers.values()
					.stream()
					.filter(w -> name.equals(w.getName()))
					.collect(Collectors.toList());
		}
		
		return null;
	}

	/**
	 * 모든 쓰레드를 종료한다. 또한 주어진 시간동안 쓰레드들이 종료되길 기다린다.
	 * 기다리는 동안에는 이 함수는 blocking 된다.
	 */
	public void shutdown()
	{
		try
		{
			logger.info("[THREADPOOL] shutdown !!! ");

			// 더이상 작업등록이 되지 않도록 한다.
			this.threadPool.shutdown();

			// 현재 동작하는 모든 작업에 강제중지 명령을 내린다.
			this.workers.values().forEach(w -> w.stop(true));

			// 모든 쓰레드들이 멈출 때까지 대기한다.
			long waitTime = 10;
			try { waitTime = Long.parseLong(System.getProperty("jdd.thread.pool.awaitTermination", "10")); } catch(Exception e) { }

			if ( this.threadPool.awaitTermination(waitTime, TimeUnit.SECONDS) )
			{
				logger.info("[THREADPOOL] All thread is halted");
			}
			else
			{
				logger.warn("[THREADPOOL] Some thread is still alive !");
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 쓰레드로 돌려지는 인터페이스 {@link Runnable}에 추가로 몇가지를 더했다.
	 *
	 */
	public class WorkerImpl extends Worker
	{
		/**
		 * @param name: 쓰레드 이름
		 * @param job: 작업...
		 * @param local: 로컬...
		 */
		public WorkerImpl(String name, JobRunnable job, JobLocal local)
		{
			this.name = name;
			this.job = job;
			this.lazyLocal = local;
		}

		/**
		 * 쓰레드에 의해서 호출되는 함수
		 */
		@Override
		public void run()
		{
			try
			{
				// 쓰레드 구동시 필요한 정보를 확인한다.
				if ( this.job == null ) throw new RejectedExecutionException("Worker[" + this.name + "] taskRunnable is null");

				// 현재 쓰레드를 확인한다. 로컬값 설정
				this.mythread = Thread.currentThread();
				if ( this.lazyLocal != null ) local.set(this.lazyLocal);

				// 쓰레드 이름을 확인한다.
				if (this.name == null) this.name = this.mythread.getName();
				else
					this.mythread.setName(this.name);

				this.alive = true;
				if ( logger.isTraceEnabled() ) logger.trace("Worker[{}] started", this.name);


				// 쓰레드가 시작했을 때, 한번 시작했음을 알려준다.
				try { this.job.setup(); }
				catch(Exception e)
				{
					logger.warn(e.getMessage(), e);
					throw new RejectedExecutionException("Worker[" + this.name + "] open error:" + e.getMessage(), e);
				}

				// 쓰레딩 작업을 진행한다.
				boolean onemore = !this.stopping;
				try
				{
					while ( onemore && !this.stopping ) onemore = this.job.working();
				}
				catch(Exception e) { logger.warn(e.getMessage(), e); }

				// 종료작업을 한다.
				try
				{
					int error = this.job.stopped(this.aborted);
					if (logger.isTraceEnabled()) logger.trace("Worker[{}] halted, code={}", this.name, error);
				}
				catch (Exception e) { logger.warn(e.getMessage(), e); }
			}

			catch(Exception e)
			{
				logger.error(e.getMessage(), e);
			}

			finally
			{
				this.alive = false;
				workers.remove(this.hashCode());
				local.set(null);
				logger.trace("Work's thread name={} has been stopped", this.name);
			}
		}

		// GETTER/SETTER methods ///////////////////////////////////////////////////
		public String getName() {
			return name;
		}

		public boolean isAlive() {
			return alive;
		}
	}
}
