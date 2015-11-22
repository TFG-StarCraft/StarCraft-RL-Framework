package com;

import java.util.concurrent.Semaphore;

public class Sync {

	private Semaphore s_postAction;
	private Semaphore s_initSync;
	private Semaphore s_restartSync;
	private Semaphore s_end;

	private Com com;

	public Sync(Com com) {
		s_postAction = new Semaphore(0);
		s_initSync = new Semaphore(0);
		s_restartSync = new Semaphore(0);
		s_end = new Semaphore(0);
		
		l_private = new Semaphore(1);
		endCheckIsAvailable = false;
		
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
	
	private Semaphore l_private;
	private boolean endCheckIsAvailable;
	
	public void signalAgentIsStarting() {
		this.s_end.release();
	}

	public void signalIsEndCanBeChecked() {
		// Take mutex
		try {
			l_private.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!endCheckIsAvailable) {
			endCheckIsAvailable = true;
			this.s_end.release();
		}

		// Release mutex
		l_private.release();
	}
	
	public void waitForEndOfIterationCanBeChecked() {
		// Take mutex
		try {
			l_private.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.s_end.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
		endCheckIsAvailable = false;
		
		// Release mutex
		l_private.release();
	}
	

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
