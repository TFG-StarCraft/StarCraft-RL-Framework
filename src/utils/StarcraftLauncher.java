package utils;

import java.io.IOException;
import java.util.List;

import com.Com;
import com.sun.jna.Pointer;

import utils.jna.JnaUtil;

public class StarcraftLauncher {

	private static final String CHAOSLAUNCHER_NAME = "Chaoslauncher";
	private static final String STARCRAFT_NAME = "Brood War";
	private static final String CLOSE_COMMAND = "cmd /c start \"\" \"C:\\Users\\Miguel\\Desktop\\forceClose.bat - Acceso directo.lnk\"";
	private static final String START_CHAOSLAUNCHER = "cmd /c start \"\" \"C:\\Users\\Miguel\\Desktop\\Chaoslauncher.lnk\"";

	public static void launchChaosLauncher(Com com) {

		// Check if chaosLauncher is already launched
		List<String> winNameList = JnaUtil.getAllWindowNames();

		if (winNameList.contains(CHAOSLAUNCHER_NAME)) {
			Pointer hWnd = JnaUtil.getWinHwnd(CHAOSLAUNCHER_NAME);
			JnaUtil.setForegroundWindow(hWnd);
		} else {
			// Launch CHAOSLAUNCHER
			try {
				Runtime.getRuntime().exec(START_CHAOSLAUNCHER);
			} catch (IOException e) {
				com.onError("No se pudo ejecutar Chaoslauncher", true);
			}
		}
	}

	public static void closeSC(Com com) {
		List<String> winNameList = JnaUtil.getAllWindowNames();

		if (winNameList.contains(STARCRAFT_NAME)) { // SC is already launched
			try {
				// Close current StarCraft
				Runtime.getRuntime().exec(CLOSE_COMMAND);
				Thread.sleep(1000);

				winNameList = JnaUtil.getAllWindowNames();
				if (winNameList.contains(STARCRAFT_NAME)) {
					// It should be closed
					com.onError("No se pudo ejecutar StarCraft, no se pudo cerrar la anterior ejecución", true);
				}

			} catch (InterruptedException e) {
				com.onError("No se pudo ejecutar StarCraft, no se pudo cerrar la anterior ejecución", true);
			} catch (IOException e1) {
				com.onError("No se pudo ejecutar StarCraft, no se pudo cerrar la anterior ejecución", true);
			}
		}
	}

}
