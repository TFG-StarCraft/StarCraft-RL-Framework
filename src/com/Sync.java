package com;

import java.util.concurrent.Semaphore;

public class Sync {

	public Semaphore s_postAction;
	public Semaphore s_initSync;
	public Semaphore s_restartSync;

	public Sync() {
		s_postAction = new Semaphore(0);
		s_initSync = new Semaphore(0);
		s_restartSync = new Semaphore(0);
	}

}
