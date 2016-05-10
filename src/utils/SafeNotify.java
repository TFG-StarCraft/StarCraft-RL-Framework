package utils;

import java.util.concurrent.Semaphore;

public class SafeNotify {

	Semaphore s;

	public SafeNotify() {
		this.s = new Semaphore(0);
	}

	public void safeWait() throws InterruptedException {
		s.acquire();
	}

	public void safeNotify() {
		s.release();
	}
}
