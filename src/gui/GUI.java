package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.Com;


public class GUI extends JFrame {

	private static final long serialVersionUID = 2941318999657277463L;

	private JPanel mainPanel;

	public GUI() {
		this.mainPanel = new ExecPanel(new Com());
		this.setContentPane(mainPanel);
	}

	public void startGui() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setVisible(true);
	}

	public static void main(String[] args) {
		GUI gui = new GUI();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				gui.startGui();
			}
		});
	}

}
