package guru.z3.temple.toolkit.concurrent;

public class WorkerUtils
{
	/**
	 * {@link Thread#interrupt()} 발생과 상관없이 일정한 시간동안 sleep상태를 유지한다
	 * @param tm
	 */
	public static void fixedSleep(long tm)
	{
		long t1 = System.currentTimeMillis();
		long t2 = tm;
		
		do 
		{
			try { Thread.sleep(t2); } catch(InterruptedException e) { }
			t2 = tm - (System.currentTimeMillis() - t1);
		} while( t2 > 1 );
	}
}
