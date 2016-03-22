package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.Com;

import qLearning.agent.Action;
import utils.BwapiConfig;
import utils.Config;
import utils.DebugEnum;
import utils.MapsSync;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	private ExecPanel mainPanel;

	class MyMenuBar extends JMenuBar {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1328830028539474345L;
		
		class FileMenu extends JMenu {

			private static final long serialVersionUID = 5375254598780074049L;
			
			private FileMenu() {
				super("File");
				JMenuItem selectStarCraftFolder = new JMenuItem("StarCraft Folder");
				selectStarCraftFolder.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(); 
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					    // disable the "All files" option.
						fc.setAcceptAllFileFilterUsed(false);
						int r = fc.showOpenDialog(GUI.this);
						
						if (r == JFileChooser.APPROVE_OPTION) {
							Config.set(Config.SC_PATH_PROP, fc.getSelectedFile().getAbsolutePath());
						}
					}
				});
				
				JMenuItem selectMapsFolder = new JMenuItem("DevMaps Folder");
				selectMapsFolder.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(); 
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					    // disable the "All files" option.
						fc.setAcceptAllFileFilterUsed(false);
						int r = fc.showOpenDialog(GUI.this);
						
						if (r == JFileChooser.APPROVE_OPTION) {
							Config.set(Config.SC_DEV_MAPS_PATH_PROP, fc.getSelectedFile().getAbsolutePath());
						}
					}
				});
				
				JMenuItem selectCurrentMap = new JMenuItem("Choose map");
				selectCurrentMap.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(new File(Config.get(Config.SC_DEV_MAPS_PATH_PROP))); 
						fc.setFileFilter(new FileFilter() {
							
							@Override
							public String getDescription() {
								return "StarCraft map files";
							}
							
							@Override
							public boolean accept(File f) {
								return f.isDirectory() || f.getName().endsWith(".scm") || f.getName().endsWith(".scx");
							}
						});
					    // disable the "All files" option.
						fc.setAcceptAllFileFilterUsed(false);
						int r = fc.showOpenDialog(GUI.this);
						
						if (r == JFileChooser.APPROVE_OPTION) {
							Config.set(Config.SC_CURRENT_MAP, fc.getSelectedFile().getName());
							
							BwapiConfig.rewriteBwapi_ini("map = ", fc.getSelectedFile().getAbsolutePath());
						}
					}
				});
				

				this.add(selectCurrentMap);
				this.addSeparator();
				this.add(selectStarCraftFolder);
				this.add(selectMapsFolder);
			}
		}
		
		class DebugMenu extends JMenu {
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

		class ActionsSelectedMenu extends JMenu {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7454100084035621587L;

			private ActionsSelectedMenu() {
				super("ActionsSelected");
				
				for (int i = 0; i < Action.ActionEnum.values().length; i++) {
					Action.ActionEnum e = Action.ActionEnum.values()[i];
					JCheckBoxMenuItem cb = new JCheckBoxMenuItem(e.toString());
					cb.setSelected(Action.isSelected(e));
					cb.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							if (cb.isSelected()) {
								Action.addToMask(e);
							} else {
								Action.removeFromMask(e);
							}
						}
					});

					this.add(cb);
				}
			}
		}
		
		DebugMenu debugMenu;
		FileMenu fileMenu;
		ActionsSelectedMenu actionMenu;

		private MyMenuBar() {
			this.debugMenu = new DebugMenu();
			this.fileMenu = new FileMenu();
			this.actionMenu = new ActionsSelectedMenu();
			this.add(fileMenu);
			this.add(debugMenu);
			this.add(actionMenu);
		}
	}

	MyMenuBar menuBar;

	public GUI() {
		super("Starcraft GUI ML Launcher");
		this.setSize(575,675);
		this.setMinimumSize(new Dimension(575,575));
		this.mainPanel = new ExecPanel(this, new Com());
		this.menuBar = new MyMenuBar();
		this.setContentPane(mainPanel);
	}

	public void startGui() {
		this.setSize(575,675);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setJMenuBar(menuBar);

		this.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		File folder = new File(new File("").getAbsolutePath());
		ArrayList<String> a = new ArrayList<>();
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".lnk")) {
				a.add(listOfFiles[i].getName());
			}
		}

		if (!a.contains(utils.StarcraftLauncher.CHAOSLAUNCHER_LNK) || !a.contains(utils.StarcraftLauncher.CLOSE_LNK)) {
			System.err.println("ERROR: se necesita (en el directorio de ejecuci�n) los siguientes accesos directos:\n"
					+ "\t" + utils.StarcraftLauncher.CHAOSLAUNCHER_LNK + " que apunte a Chaoslauncher.exe, CON PRIVILEGIOS DE ADMINISTRADOR\n"
					+ "\t" + utils.StarcraftLauncher.CLOSE_LNK + " que apunte al forceClose.bat de este directorio,\n"
					+ "\t\tCON PRIVILEGIOS DE ADMINISTRADOR");
		} else {

			Config.init();
			MapsSync.sync();
			Action.loadMask();

			System.out.println(Config.get(Config.SC_PATH_PROP));
			System.out.println(Config.get(Config.SC_DEV_MAPS_PATH_PROP));
			
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
