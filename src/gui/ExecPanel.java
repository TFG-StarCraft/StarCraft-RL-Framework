package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import com.Com;
import com.observers.ComObserver;
import javax.swing.JToggleButton;

public class ExecPanel extends JPanel implements ComObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -624314441968368833L;

	private boolean showDebug;
	
	private JPanel topPanel;
	private JButton run;
	private JLabel l_alpha, l_gamma, l_epsilon;
	private JTextField t_alpha, t_gamma, t_epsilon;

	private JScrollPane scroll;
	private JTextArea textConsole;

	private Com com;
	private JPanel panel;
	private JPanel panel_1;
	private JButton btnShutsc;
	private JToggleButton tglbtnGui;
	private JLabel lblFps;
	private JLabel lblSpeed;
	private JTextField textFieldSpeed;

	public ExecPanel(Com com, boolean showDebug) {
		this.com = com;
		this.com.addObserver(this);
		this.showDebug = showDebug;

		this.topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 1, 0, 0));

		panel = new JPanel();
		topPanel.add(panel);
		panel.setLayout(new GridLayout(0, 6, 10, 0));

		this.l_alpha = new JLabel("Alpha: ");
		panel.add(l_alpha);
		this.t_alpha = new JTextField();
		panel.add(t_alpha);
		this.t_alpha.setText(Double.toString(qLearning.Const.ALPHA));
		this.l_gamma = new JLabel("Gamma: ");
		panel.add(l_gamma);
		this.t_gamma = new JTextField();
		panel.add(t_gamma);
		this.t_gamma.setText(Double.toString(qLearning.Const.GAMMA));
		this.l_epsilon = new JLabel("Epsilon: ");
		panel.add(l_epsilon);
		this.t_epsilon = new JTextField();
		panel.add(t_epsilon);
		this.t_epsilon.setText(Double.toString(qLearning.Const.EPSLLON_EGREEDY));

		panel_1 = new JPanel();
		topPanel.add(panel_1);
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

					run.setEnabled(false);
					new Thread(com).start();
				} catch (NumberFormatException e1) {
					t_alpha.setText(Double.toString(qLearning.Const.ALPHA));
					t_gamma.setText(Double.toString(qLearning.Const.GAMMA));
					t_epsilon.setText(Double.toString(qLearning.Const.EPSLLON_EGREEDY));
				}
			}
		});
		
		lblSpeed = new JLabel("Speed:");
		panel_1.add(lblSpeed);
		
		textFieldSpeed = new JTextField();
		panel_1.add(textFieldSpeed);
		textFieldSpeed.setText("0");
		textFieldSpeed.setColumns(5);
		textFieldSpeed.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				try {
					int n = Integer.parseInt(textFieldSpeed.getText());
					if (n < 0)
						throw new NumberFormatException();
					com.bot.frameSpeed = Integer.parseInt(textFieldSpeed.getText());
				} catch (NumberFormatException e1) {
					textFieldSpeed.setText(com.bot.frameSpeed + "");
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lblFps = new JLabel("FPS:");
		panel_1.add(lblFps);
		panel_1.add(run);

		tglbtnGui = new JToggleButton("GUI");
		tglbtnGui.setSelected(true);
		tglbtnGui.addActionListener(new ActionListener() {

			private boolean b = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				com.bot.guiEnabled = !b;
				b = !b;
				tglbtnGui.setSelected(b);
			}
		});
		panel_1.add(tglbtnGui);

		btnShutsc = new JButton("ShutSc");
		btnShutsc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				com.shutSc();
			}
		});
		panel_1.add(btnShutsc);

		this.textConsole = new JTextArea();
		this.textConsole.setEditable(false);
		this.scroll = new JScrollPane(textConsole);
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.add(this.topPanel, BorderLayout.NORTH);
		this.add(this.scroll, BorderLayout.CENTER);

		DefaultCaret caret = (DefaultCaret) textConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	@Override
	public void onEndIteration(int i, int movimientos, int nume) {
		// TODO Auto-generated method stub
		this.textConsole.append("movimientos: " + movimientos + " nume: " + nume + " episodio " + i + "\n");
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
		this.textConsole.append(s + "\n");
	}

	@Override
	public void onError(String s, boolean fatal) {
		JOptionPane.showMessageDialog(this.getParent(), s, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void onFpsAverageAnnouncement(double fps) {
		this.lblFps.setText("FPS: " + Double.toString(fps));
	}

	@Override
	public void onDebugMessage(String s) {
		if (showDebug)
			onSendMessage(s);
	}

}
