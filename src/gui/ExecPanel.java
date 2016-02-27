package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import org.math.plot.Plot2DPanel;
import com.Com;
import com.observers.ComObserver;

import utils.DebugEnum;

import javax.swing.JToggleButton;

/**
 * Class with the panels of the GUI.
 * 
 * @author Alberto Casas Ortiz.
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez.
 */
public class ExecPanel extends JPanel implements ComObserver {
	/** Generated serial version ID. */
	private static final long serialVersionUID = -624314441968368833L;
	/** Debug mask. */
	private long debugMask;
	/** Comunication. */
	private Com com;

	/** Labels of parameter. */
	private JLabel l_alpha, l_gamma, l_epsilon;
	/** Text field of parameter. */
	private JTextField t_alpha, t_gamma, t_epsilon;

	/** Scroll panel for the text console. */
	private JScrollPane scroll;
	/** Text console. */
	private JTextArea textConsole;

	/** Tabbed pane for tabs. */
	private JTabbedPane topTabbedPanel;
	/** Panel with the console. */
	private JPanel panelConsole;
	/** Panel with the graph. */
	private JPanel panelGraphic;
	/** Panel with buttons and console. */
	private JPanel contentPanel;
	/** Panel with the buttons */
	private JPanel panelButtons;

	/** Run button. */
	private JButton run;
	/** Shut starcraft button. */
	private JButton btnShutsc;
	/** Button for disable gui. */
	private JToggleButton tglbtnGui;

	/** Label for frames per second. */
	private JLabel lblFps;

	/** Label for speed. */
	private JLabel lblSpeed;
	/** Text field for speed. */
	private JTextField textFieldSpeed;

	/** Panel with the plot of the movements per iteration. */
	private Plot2DPanel graphic_movements;

	/** Array with the movements. */
	private double[] movements;
	/** Array with the iterations. */
	private double[] iterations;
	/** Length of the arrays. */
	private int lengthData;


	private boolean b = true;
	private int frameSpeed;
	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/

	/**
	 * Constructor of the class Execpanel.
	 * 
	 * @param com
	 *            = Comunication.
	 */
	public ExecPanel(Com com) {
		this.com = com;
		this.com.addObserver(this);
		this.debugMask = 0;

		this.topTabbedPanel = new JTabbedPane();
		this.topTabbedPanel.setSize(getWidth(), getHeight());
		this.graphic_movements = new Plot2DPanel();
		this.movements = new double[0];
		this.iterations = new double[0];

		panelConsole = new JPanel();
		panelGraphic = new JPanel();
		panelButtons = new JPanel();
		this.panelButtons.setLayout(new GridBagLayout());
		this.l_alpha = new JLabel("Alpha: ", SwingConstants.RIGHT);
		this.t_alpha = new JTextField();
		this.l_gamma = new JLabel("Gamma: ", SwingConstants.RIGHT);
		this.t_gamma = new JTextField();
		this.l_epsilon = new JLabel("Epsilon: ", SwingConstants.RIGHT);
		this.t_epsilon = new JTextField();

		contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());

		run = new JButton("Run");
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double alpha, gamma, epsilon;

				try {
					alpha = Double.parseDouble(t_alpha.getText());
					if (alpha < 0 || alpha >= 1)
						throw new NumberFormatException();
					gamma = Double.parseDouble(t_gamma.getText());
					if (gamma < 0 || gamma >= 1)
						throw new NumberFormatException();
					epsilon = Double.parseDouble(t_epsilon.getText());
					if (epsilon < 0 || epsilon >= 1)
						throw new NumberFormatException();
					com.configureParams(alpha, gamma, epsilon);
					com.configureBot(b,frameSpeed);

					tglbtnGui.setEnabled(true);
					run.setEnabled(false);
					new Thread(com).start();
				} catch (NumberFormatException e1) {
					t_alpha.setText(Double.toString(qLearning.Const.ALPHA));
					t_gamma.setText(Double.toString(qLearning.Const.GAMMA));
					t_epsilon.setText(Double.toString(qLearning.Const.EPSLLON_EGREEDY));
				}
			}
		});

		lblSpeed = new JLabel("Speed:", SwingConstants.RIGHT);

		textFieldSpeed = new JTextField();
		textFieldSpeed.setText("0");
		textFieldSpeed.setColumns(5);
		textFieldSpeed.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				try {
					int n = Integer.parseInt(textFieldSpeed.getText());
					if (n < 0)
						throw new NumberFormatException();
					frameSpeed = n;
					try {
						com.bot.frameSpeed = Integer.parseInt(textFieldSpeed.getText());
					} catch (NullPointerException e1) {
					}
				} catch (NumberFormatException e1) {
					textFieldSpeed.setText(com.bot.frameSpeed + "");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		lblFps = new JLabel("FPS:", SwingConstants.CENTER);

		tglbtnGui = new JToggleButton("GUI");

		tglbtnGui.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (com.bot != null) {
					com.bot.guiEnabled = !b;
					b = !b;
					tglbtnGui.setSelected(b);
				}
			}
		});

		// TODO
		btnShutsc = new JButton("ShutSc");
		btnShutsc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				com.shutSc();
			}
		});

		this.textConsole = new JTextArea();
		this.textConsole.setEditable(false);
		this.scroll = new JScrollPane(textConsole);
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.add(this.topTabbedPanel, BorderLayout.CENTER);
		createConsolePanel();
		createGraphicPanel();
		DefaultCaret caret = (DefaultCaret) textConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	/**************/
	/* GUI METHOD */
	/**************/

	/**
	 * Create the panel with the graph.
	 */
	public void createGraphicPanel() {
		panelGraphic.setLayout(new GridLayout(1, 1));
		topTabbedPanel.addTab("Graphic", panelGraphic);
		this.graphic_movements.setAxisLabels("Episodes", "Movs");
		this.panelGraphic.add(this.graphic_movements);
	}

	/**
	 * Create the panel with the console.
	 */
	public void createConsolePanel() {
		panelConsole.setLayout(new GridBagLayout());
		topTabbedPanel.addTab("Console", panelConsole);

		GridBagConstraints c = new GridBagConstraints();

		// Add parameters alpha, epsillon and lambda.
		c.weightx = c.weighty = 0.1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		panelConsole.add(l_alpha, c);
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 70);
		panelConsole.add(t_alpha, c);
		this.t_alpha.setText(Double.toString(qLearning.Const.ALPHA));

		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		panelConsole.add(l_gamma, c);
		c.gridx = 3;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 70);
		panelConsole.add(t_gamma, c);
		this.t_gamma.setText(Double.toString(qLearning.Const.GAMMA));

		c.gridx = 4;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		panelConsole.add(l_epsilon, c);
		c.gridx = 5;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 70);
		panelConsole.add(t_epsilon, c);
		this.t_epsilon.setText(Double.toString(qLearning.Const.EPSLLON_EGREEDY));

		// Add the panel with the rest of components.
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		panelConsole.add(contentPanel, c);

		// Add the information of speed and fps.
		c.weightx = c.weighty = 0.1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(lblFps, c);

		c.gridx = 1;
		c.gridy = 0;
		contentPanel.add(lblSpeed, c);
		c.gridx = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		contentPanel.add(textFieldSpeed, c);

		// Add buttons of the gui.
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panelButtons.add(run, c);
		c.gridx = 1;
		c.gridy = 0;
		panelButtons.add(tglbtnGui, c);
		c.gridx = 2;
		c.gridy = 0;
		panelButtons.add(btnShutsc, c);

		// Add the panel with the buttons.
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(this.panelButtons, c);

		// Add the scroll panel with the text view of the console.
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = c.weighty = 1.0;
		c.insets = new Insets(10, 1, 1, 1);
		c.fill = GridBagConstraints.BOTH;
		contentPanel.add(this.scroll, c);
		tglbtnGui.setEnabled(false);
		tglbtnGui.setSelected(true);

	}

	/*******************/
	/* GETTER / SETTER */
	/*******************/

	/**
	 * Set a debug mask.
	 * 
	 * @param debugMask
	 *            = New debug mask.
	 */
	public void setDebugMask(long debugMask) {
		this.debugMask = debugMask;
	}

	/**
	 * Get debug mask.
	 * 
	 * @return The debug mask.
	 */
	public long getDebugMask() {
		return debugMask;
	}

	/******************/
	/* PRIVATE METHOD */
	/******************/

	/**
	 * Resize the arrays of movements and iterations.
	 */
	private void resize() {
		double[] movAux, iterAux;
		movAux = new double[lengthData + 1];
		iterAux = new double[lengthData + 1];

		for (int i = 0; i < this.lengthData; i++) {
			movAux[i] = movements[i];
			iterAux[i] = iterations[i];
		}
		this.lengthData++;
		this.movements = movAux;
		this.iterations = iterAux;
	}

	/*******************/
	/* OVERRIDE METHOD */
	/*******************/

	/**
	 * Listener on end of and iteration.
	 * 
	 * @param i
	 *            = Iteration number.
	 * @param movimientos
	 *            = Number of movements.
	 * @param nume
	 *            = Number of random movements.
	 */
	@Override
	public void onEndIteration(int i, int movimientos, int nume) {
		// TODO Auto-generated method stub
		this.textConsole.append("movimientos: " + movimientos + " nume: " + nume + " episodio " + i + "\n");
		this.resize();
		// Save movements.
		this.movements[i] = movimientos;
		this.iterations[i] = i;
		// Paint the plot.
		this.panelGraphic.remove(this.graphic_movements);
		this.graphic_movements = new Plot2DPanel();
		this.graphic_movements.addLinePlot("Movements per iteration.", Color.BLUE, this.iterations, this.movements);
		this.graphic_movements.setAxisLabels("Episodes", "Movs");
		this.panelGraphic.add(this.graphic_movements);
	}

	/**
	 * Listener on sending a message.
	 * 
	 * @param s
	 *            = Message to send.
	 */
	@Override
	public void onSendMessage(String s) {
		this.textConsole.append(s + "\n");
	}

	/**
	 * Listener on error.
	 * 
	 * @param s
	 *            = Message of the error.
	 * @param fatal
	 *            = True if the error is fatal.
	 */
	@Override
	public void onError(String s, boolean fatal) {
		JOptionPane.showMessageDialog(this.getParent(), s, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Listener on FPS average announcement.
	 * 
	 * @param fps
	 *            = Frames per second.
	 */
	@Override
	public void onFpsAverageAnnouncement(double fps) {
		this.lblFps.setText("FPS: " + Double.toString(fps));
	}

	/**
	 * Listener on debug message.
	 * 
	 * @param s
	 *            = Message.
	 * @param level
	 *            = Level of the message.
	 */
	@Override
	public void onDebugMessage(String s, DebugEnum level) {
		if ((debugMask & (1 << level.ordinal())) != 0)
			onSendMessage(s);
	}

	/*****************/
	/* UNUSED METHOD */
	/*****************/

	/**
	 * Listener on end train.
	 */
	@Override
	public void onEndTrain() {
		// TODO Auto-generated method stub
	}

	/**
	 * Listener on action taken.
	 */
	@Override
	public void onActionTaken() {
		// TODO Auto-generated method stub
	}

	/**
	 * Listener on action fail.
	 */
	@Override
	public void onActionFail() {
		// TODO Auto-generated method stub
	}

}
