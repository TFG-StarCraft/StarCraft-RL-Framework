package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import com.Com;

import utils.DebugEnum;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	private ExecPanel mainPanel;

	private class MyMenuBar extends JMenuBar {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1328830028539474345L;

		private class DebugMenu extends JMenu {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7454100084035621587L;

			private DebugMenu() {
				super("Debug");
				for (int i = 0; i < DebugEnum.values().length; i++) {
					DebugEnum e = DebugEnum.values()[i];
					JCheckBoxMenuItem cb = new JCheckBoxMenuItem(e.toString());
					cb.setSelected((mainPanel.getDebugMask() & e.getMask()) != 0);
					cb.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							if (cb.isSelected()) {
								mainPanel.setDebugMask(mainPanel.getDebugMask() | e.getMask());
							} else {
								mainPanel.setDebugMask(mainPanel.getDebugMask() & ~e.getMask());
							}
						}
					});

					this.add(cb);
				}
			}
		}

		private DebugMenu debugMenu;

		private MyMenuBar() {
			this.debugMenu = new DebugMenu();
			this.add(debugMenu);
		}
	}

	private MyMenuBar menuBar;

	public GUI() {
		super("Starcraft GUI ML Launcher");
		this.mainPanel = new ExecPanel(new Com());
		this.menuBar = new MyMenuBar();
		this.setContentPane(mainPanel);
	}

	public void startGui() {
		this.setSize(575,675);
		this.setResizable(false);
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
					+ "\tforceClose.bat.lnk que apunte al forceClose.bat de este directorio,\n"
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
