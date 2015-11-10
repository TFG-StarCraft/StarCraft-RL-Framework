package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.Com;
import com.ComObserver;

public class ExecPanel extends JPanel implements ComObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -624314441968368833L;
	
	private JPanel topPanel;
	private JButton run;
	private JLabel l_alpha, l_gamma, l_epsilon;
	private JTextField t_alpha, t_gamma, t_epsilon;

	private JScrollPane scroll;
	private JTextArea textConsole;

	private Com com;
	
	public ExecPanel(Com com) {
		this.com = com;
		this.com.addObserver(this);
		
		this.topPanel = new JPanel();
		
		this.l_alpha = new JLabel("Alpha: ");
		this.l_gamma = new JLabel("Gamma: ");
		this.l_epsilon = new JLabel("Epsilon: ");
		this.t_alpha = new JTextField();
		this.t_alpha.setText(Double.toString(qLearning.Const.ALPHA));
		this.t_gamma = new JTextField();
		this.t_gamma.setText(Double.toString(qLearning.Const.GAMMA));
		this.t_epsilon = new JTextField();
		this.t_epsilon.setText(Double.toString(qLearning.Const.EPSLLON_EGREEDY));
		this.run = new JButton("Run");
		this.run.addActionListener(new ActionListener() {
			
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
		
		this.topPanel.setLayout(new GridLayout(1, 7));
		this.topPanel.add(this.l_alpha);
		this.topPanel.add(this.t_alpha);
		this.topPanel.add(this.l_gamma);
		this.topPanel.add(this.t_gamma);
		this.topPanel.add(this.l_epsilon);
		this.topPanel.add(this.t_epsilon);
		this.topPanel.add(this.run);
		
		this.textConsole = new JTextArea();
		this.textConsole.setEditable(false);
		this.scroll = new JScrollPane(textConsole);
		this.scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.setLayout(new BorderLayout());
		this.add(this.topPanel, BorderLayout.NORTH);
		this.add(this.scroll, BorderLayout.CENTER);
		
	}


	@Override
	public void onEndIteration() {
		// TODO Auto-generated method stub
		
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
	
}
