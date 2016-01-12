package gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.Com;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	private ExecPanel mainPanel;

	private class MyMenuBar extends JMenuBar {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1328830028539474345L;
		private JCheckBoxMenuItem showDebug;
		
		private MyMenuBar() {
			this.showDebug = new JCheckBoxMenuItem("Show debug info");
			this.showDebug.setSelected(mainPanel.showDebug);
			this.showDebug.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					mainPanel.showDebug = showDebug.isSelected();
				}
			});
			this.add(showDebug);
		}
	}
	
	private MyMenuBar menuBar;
	
	public GUI() {
		this.mainPanel = new ExecPanel(new Com(), false);
		this.menuBar = new MyMenuBar();
		this.setContentPane(mainPanel);
	}

	public void startGui() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setJMenuBar(menuBar);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		File folder = new File(new File("").getAbsolutePath());
		ArrayList<String> a = new ArrayList<>();
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".lnk")) {
				a.add(listOfFiles[i].getName());
			}
		}

		if (!a.contains("Chaoslauncher.lnk") || !a.contains("forceClose.bat - Acceso directo.lnk")) {
			System.err.println("ERROR: se necesita (en el directorio de ejecución) los siguientes accesos directos:\n"
					+ "\tChaoslauncher.lnk que apunte a Chaoslauncher.exe, CON PRIVILEGIOS DE ADMINISTRADOR\n"
					+ "\tforceClose.bat - Acceso directo.lnk que apunte al forceClose.bat de este directorio,\n"
					+ "\t\tCON PRIVILEGIOS DE ADMINISTRADOR");
		} else {

			GUI gui = new GUI();

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					gui.startGui();
				}
			});
		}
	}

}
