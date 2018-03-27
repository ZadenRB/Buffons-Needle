package needle.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Main {

	protected static int sleepTime;
	protected static Thread t;
	protected static boolean running;

	public static boolean isNum(String str) {
		int size = str.length();
		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return size > 0;
	}

	public static boolean tossNeedle(NeedlesComponent comp) {
		double locationX = Math.random();
		double locationY = Math.random();
		double angle = Math.toRadians(Math.random() * 360);
		double height = Math.abs(0.5 * Math.sin(angle));
		double width = Math.abs(0.5 * Math.cos(angle));
		double x1 = locationX * 500 - width * 100;
		double x2 = locationX * 500 + width * 100;
		double y1 = locationY * 500 - height * 100;
		double y2 = locationY * 500 + height * 100;
		double hundredsX1 = (int) (x1 / 100);
		double hundredsX2 = (int) (x2 / 100);
		comp.addNeedle(x1, y1, x2, y2);
		if (hundredsX1 != hundredsX2 || (int) Math.signum(x1) != (int) Math.signum(x2))
			return true;
		return false;
	}

	public static void simulate(String input, JLabel status, NeedlesComponent comp, JButton sim, JButton clr,
			JButton can) {
		t = new Thread() {
			@Override
			public void run() {
				comp.clearNeedles();
				if (isNum(input)) {
					long needles = Long.valueOf(input);
					double matches = 0;
					for (long i = 1; i <= needles; i++) {
						if (!running || sleepTime == 0) {
							comp.clearNeedles();
							simulateInstant(input, status, comp, sim, clr, can);
							break;
						}
						if (tossNeedle(comp)) {
							matches++;
						}
						comp.repaint();
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						status.setText("Pi = " + (i / matches) * 2);
						status.repaint();
					}
				} else {
					status.setText("<html><font color=red>Please input a whole number</font></html>");
				}
				sim.setEnabled(true);
				clr.setEnabled(true);
				can.setEnabled(false);
			}
		};
		t.start();
	}

	public static void simulateInstant(String input, JLabel status, NeedlesComponent comp, JButton sim, JButton clr,
			JButton can) {
		if (isNum(input)) {
			comp.clearNeedles();
			long needles = Long.valueOf(input);
			double matches = 0;
			for (long i = 1; i <= needles; i++) {
				if (tossNeedle(comp)) {
					matches++;
				}
			}
			status.setText("Pi = " + (needles / matches) * 2);
		} else {
			status.setText("<html><font color=red>Please input a whole number</font></html>");
		}
		comp.repaint();
		sim.setEnabled(true);
		clr.setEnabled(true);
		can.setEnabled(false);
	}

	public static void createAndShowGUI() {
		sleepTime = 200;
		running = true;
		final NeedlesComponent comp = new NeedlesComponent();
		JFrame frame = new JFrame("Buffon's Needle");
		JPanel board = new JPanel(new BorderLayout());
		JPanel input = new JPanel(new GridLayout(3, 2));
		JLabel status = new JLabel("Input a number, then click simulate:", JLabel.CENTER);
		JTextField in = new JTextField(JTextField.CENTER);
		JButton sim = new JButton("Simulate");
		JButton clr = new JButton("Clear");
		JButton can = new JButton("Cancel");
		String[] speedOptions = { "Normal", "Fast", "Instant" };
		JComboBox<String> spd = new JComboBox<String>(speedOptions);

		// Board JPanel
		board.add(comp, BorderLayout.CENTER);
		board.setPreferredSize(new Dimension(500, 500));

		// Input JPanel
		in.setHorizontalAlignment(SwingConstants.CENTER);
		can.setEnabled(false);

		input.add(status);
		input.add(in);
		input.add(clr);
		input.add(sim);
		input.add(can);
		input.add(spd);
		input.setPreferredSize(new Dimension(500, 103));

		// Add components
		frame.add(board, BorderLayout.CENTER);
		frame.add(input, BorderLayout.PAGE_END);

		// JFrame settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		/* Button listeners */
		// Clear button
		clr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status.setText("Input a number, then click simulate");
				in.setText("");
				comp.clearNeedles();
			}
		});

		// Simulate button
		sim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = in.getText();
				running = true;
				sim.setEnabled(false);
				clr.setEnabled(false);
				can.setEnabled(true);
				if (sleepTime > 0) {
					simulate(input, status, comp, sim, clr, can);
				} else {
					simulateInstant(input, status, comp, sim, clr, can);
				}
			}
		});

		// Cancel button
		can.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				running = false;
				sim.setEnabled(true);
				clr.setEnabled(true);
				can.setEnabled(false);
				comp.clearNeedles();
				try {
					t.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				status.setText("Input a number, then click simulate");
				status.repaint();
			}
		});

		// Speed selection
		spd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				switch ((String) cb.getSelectedItem()) {
				case "Normal":
					sleepTime = 200;
					break;
				case "Fast":
					sleepTime = 100;
					break;
				case "Instant":
					sleepTime = 0;
					break;
				}
			}
		});
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
