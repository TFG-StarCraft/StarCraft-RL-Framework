package utils.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.win32.StdCallLibrary;

/**
 * JNA interface with Window's user32.dll
 * 
 * @author Pete S
 * 
 */
public interface User32 extends StdCallLibrary {
	User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

	interface WNDENUMPROC extends StdCallCallback {
		boolean callback(Pointer hWnd, Pointer arg);
	}

	public static final int GW_OWNER = 4; // used with GetWindow to get win
											// owner
	public static final int GW_HWNDNEXT = 2; // used with GetNextWindow
	public static final int GA_ROOT = 2; // used with GetAncestor
	public static final int GWL_EXSTYLE = -20; // used with GetWindowLong
	public static final long WS_EX_APPWINDOW = 0x00040000L;
	public static final Pointer HWND_TOP = new Pointer(0L); // used with
															// SetWindowPos

	boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

	int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

	int SetForegroundWindow(Pointer hWnd);

	Pointer GetForegroundWindow();

	boolean GetWindowRect(Pointer hWnd, RECT rect);

	boolean SetWindowPos(Pointer hWnd, Pointer hWndInsertAfter, int x, int y, int cx, int cy, int uFlags);

	boolean MoveWindow(Pointer hWnd, int x, int y, int nWidth, int nHeight, boolean bRepaint);

	boolean IsWindow(Pointer hWnd);

	Pointer GetWindow(Pointer hWnd, int uCmd);

	LONG_PTR GetWindowLongPtr(HWND hWnd, int nIndex);

	Pointer GetParent(Pointer hWnd);

	Pointer GetAncestor(Pointer hWnd, int gaFlags);

	boolean IsWindowVisible(Pointer hWnd);
}
