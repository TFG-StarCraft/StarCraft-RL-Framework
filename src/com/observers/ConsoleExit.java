package com.observers;

public class ConsoleExit implements ComObserver {

	@Override
	public void onEndIteration(int i, int movimientos, int nume) {
		System.out.println("movimientos: " + movimientos + " nume: " + nume + " episodio " + i);
	}

	@Override
	public void onEndTrain() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionTaken() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionFail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendMessage(String s) {
		System.out.println(s);
	}

	@Override
	public void onError(String s, boolean fatal) {
		System.err.println(s);
	}

}