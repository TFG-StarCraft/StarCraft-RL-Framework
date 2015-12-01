package utils.jna;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public class JnaUtil {
	
	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		interface WNDENUMPROC extends StdCallCallback {
			boolean callback(Pointer hWnd, Pointer arg);
		}

		boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);
		int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
		int SetForegroundWindow(Pointer hWnd);
	}
	
	private static final User32 user32 = User32.INSTANCE;
	private static Pointer callBackHwnd;

	public static List<String> getAllWindowNames() {
		final List<String> windowNames = new ArrayList<String>();
		user32.EnumWindows(new User32.WNDENUMPROC() {

			@Override
			public boolean callback(Pointer hWnd, Pointer arg) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText).trim();
				if (!wText.isEmpty()) {
					windowNames.add(wText);
				}
				return true;
			}
		}, null);

		return windowNames;
	}

	public static Pointer getWinHwnd(final String startOfWindowName) {
		callBackHwnd = null;

		user32.EnumWindows(new User32.WNDENUMPROC() {
			@Override
			public boolean callback(Pointer hWnd, Pointer userData) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText).trim();

				if (!wText.isEmpty() && wText.startsWith(startOfWindowName)) {
					callBackHwnd = hWnd;
					return false;
				}
				return true;
			}
		}, null);
		return callBackHwnd;
	}

	public static boolean setForegroundWindow(Pointer hWnd) {
		return user32.SetForegroundWindow(hWnd) != 0;
	}
}
