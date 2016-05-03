package utils;

public class SafeNotify {

	private int code;
	private Object o;
	
	public SafeNotify(Object o) {
		this.o = o;
	}
	
	public void safeWait(int code) throws InterruptedException {
		synchronized (o) {
			
			this.code = code;
			
			o.wait();			
		}
	}
	
	public void safeNotify(int code) {
		synchronized (o) {
			
			if (this.code != code)
				throw new RuntimeException("Code was " + code + ", expected " + this.code);
			
			o.notify();			
		}
	}
	
}
