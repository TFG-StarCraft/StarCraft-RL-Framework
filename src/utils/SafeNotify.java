package utils;

import java.util.concurrent.Semaphore;

public class SafeNotify {

	private int code;
	private Object o;

	Semaphore s;

	public SafeNotify(Object o) {
		this.o = o;
		this.code = -1;
		this.s = new Semaphore(0);
	}

	public void safeWait(int code) throws InterruptedException {

		this.code = code;

		s.acquire();

	}

	public void safeNotify(int code) {

		s.release();

	}

}
