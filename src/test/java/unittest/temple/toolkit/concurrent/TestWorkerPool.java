package unittest.temple.toolkit.concurrent;

import guru.z3.temple.toolkit.concurrent.JobLocal;
import guru.z3.temple.toolkit.concurrent.JobRunnable;
import guru.z3.temple.toolkit.concurrent.Worker;
import guru.z3.temple.toolkit.concurrent.WorkerPool;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jaeda on 2017. 1. 30..
 */
public class TestWorkerPool
{
	private WorkerPool pool;

	@Before
	public void ready()
	{
		// 환경변수에서 필요한 정보를 읽어온다.
		int coreSize = 10;
		int maxSize = 100;
		int queueSize = 5;
		long keepAliveTime = 60L;

		// 쓰레드풀 생성
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSize);
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, queue);

		this.pool = new WorkerPool(tpe);
	}

	@Test
	public void testRun()
	{
		this.pool.execute("T1", new TestJob(20));
		this.pool.shutdown();
	}

	@Test
	public void testLocal()
	{
		this.pool.execute("Parent", new ParentJob(), new TestLocal());
		try { Thread.sleep(30000); } catch(InterruptedException e) { }
	}


	private class TestJob implements JobRunnable
	{
		private int max;

		public TestJob(int max)
		{
			this.max = max;
		}

		@Override
		public void setup() throws RejectedExecutionException
		{
			System.out.println("TestJob::open");
		}

		@Override
		public boolean working()
		{
			try { Thread.sleep(1000); } catch(InterruptedException e) { }

			JobLocal vv = Worker.getJobLocal();
			System.out.println("local:" + Worker.getThreadName() + "=" + vv);
			if ( vv != null && vv instanceof TestLocal )
			{
				TestLocal tt = (TestLocal)vv;
				if ( tt.increase() > this.max ) return false;
			}

			return true;
		}

		@Override
		public int stopped(boolean aborted)
		{
			System.out.println("TestJob::halt, aborted=" + aborted);
			return 0;
		}
	}

	private class ParentJob extends TestJob
	{
		public ParentJob()
		{
			super(100);
		}

		@Override
		public void setup() throws RejectedExecutionException
		{
			super.setup();
			System.out.println("run child");
			pool.execute("Child", new TestJob(30));
		}
	}

	private class TestLocal implements JobLocal
	{
		private AtomicInteger num;

		public TestLocal()
		{
			this.num = new AtomicInteger();
		}

		public int increase() { return this.num.incrementAndGet(); }

		@Override
		public String toString()
		{
			return new StringBuilder()
						.append("[TL] num=").append(this.num.get())
						.toString();
		}
	}
}
