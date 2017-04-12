package guru.z3.temple.toolkit.concurrent;

/**
 * This class is agent of {@link JobRunnable}, It will be running in thread and ...
 */
public abstract class Worker implements Runnable
{
	// TODO check change "local" on child thread
	// TODO 설계를 개선할 필요가 있다. 현재 이 객체를 WorkerPool쪽에 inner-class로 하고 싶은데, local변수를 선언할 수 없어서 이리러 뺀 상황임.
	protected static InheritableThreadLocal<JobLocal> local = new InheritableThreadLocal<JobLocal>();

	/** 작업자이름 = 쓰레드이름 */
	protected String name;

	/** true이면 살아있음 아니면 중지됨 */
	protected boolean alive;
	/** 중지요청이 되어서 중지 작업중임 */
	protected boolean stopping;
	/** 강제동료 여부 */
	protected boolean aborted;
	/** 자신의 쓰레드 */
	protected Thread mythread;
	/** 타스크 작업정의, {@link JobRunnable}구현객체 */
	protected JobRunnable job;

	/**
	 * local변수에 할당을 하려면 자신의 쓰레드가 돌고 있을 때, 해야 한다 따라서 쓰레드가 돌기 전에
	 * 갈무리 해둘 곳이 필요한데 그 곳이 이 변수이다.
	 */
	protected JobLocal lazyLocal;


	/**
	 * 쓰레드하에서 {@link JobLocal}값을 구한다.
	 * @return
	 */
	public static JobLocal getJobLocal()
	{
		return local.get();
	}

	/**
	 * return current Thread's name
	 */
	public static String getThreadName() { return Thread.currentThread().getName(); }

	/**
	 * 자신의 쓰레드, {@link Thread#interrupt()}함수를 호출한다.
	 */
	public void interrupt()
	{
		if ( this.mythread != null ) this.mythread.interrupt();
	}

	/**
	 * 쓰레드를 중지시킨다.
	 */
	public void stop(boolean abort)
	{
		if ( !this.alive ) return;
		this.stopping = true;
		this.aborted = abort;

		// 쓰레드가 대기중이면 깨운다.
		if ( this.mythread != null ) this.mythread.interrupt();
	}

	// GETTER/SETTER methods ///////////////////////////////////////////////////
	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return alive;
	}
}
