package unittest.toolkit.nio;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduleTest
{
	@Test
	public void test() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

		scheduler.schedule(() -> { System.out.println("Hello World!"); }, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> { System.out.println("Hello World!"); }, 2000, java.util.concurrent.TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> { System.out.println("Hello World!"); }, 3000, java.util.concurrent.TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> { System.out.println("Hello World!"); }, 4000, java.util.concurrent.TimeUnit.MILLISECONDS);

		try { Thread.sleep(10000L); } catch (InterruptedException _) {}

	}
}
