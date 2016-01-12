package com;

import java.util.concurrent.Semaphore;

public class Sync {

	private Semaphore s_postAction;
	private Semaphore s_initSync;
	private Semaphore s_restartSync;

	private Com com;

	public Sync(Com com) {
		s_postAction = new Semaphore(0);
		s_initSync = new Semaphore(0);
		s_restartSync = new Semaphore(0);

		this.com = com;
	}

	public void signalInitIsDone() {
		this.s_initSync.release();
	}

	public void waitForBotEndsInit() {
		try {
			this.s_initSync.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}
/*
	public void signalAgentIsStarting() {
		this.s_end.release();
	}

	public void signalIsEndCanBeChecked() {
		this.s_end.release();
	}

	public void waitForIsEndCanBeChecked() {
		try {
			this.s_end.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}
*/
	public void signalGameIsReady() {
		this.s_restartSync.release();
	}

	public void waitForBotGameIsStarted() {
		try {
			this.s_restartSync.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	/**
	 * Returns control to Agent
	 */
	public void signalActionEnded() {
		com.Sync.s_postAction.release();
	}

	public void waitForActionEnds() {
		try {
			this.s_postAction.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}

}
