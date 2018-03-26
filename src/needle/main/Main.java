package needle.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Main {
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
	
	public static void simulate(String input, JLabel status, NeedlesComponent comp) {
		if (isNum(input)) {
			comp.clearNeedles();
			long needles = Long.valueOf(input);
			double matches = 0;
			for (long i = 1; i <= needles; i++) {
				if (tossNeedle(comp)) {
					matches++;
				}
			}
			comp.drawNeedles();
			status.setText("Pi = " + (needles / matches) * 2);
		} else {
			status.setText("<html><font color=red>Please input a whole number</font></html>");
		}
	}
	
	public static void main(String args[]) {
		final NeedlesComponent comp = new NeedlesComponent();
		JFrame frame = new JFrame("Buffon's Needle");
		JPanel board = new JPanel(new BorderLayout());
		JPanel input = new JPanel(new GridLayout(2, 2));
		JLabel status = new JLabel("Input a number, then click simulate:", JLabel.CENTER);	
		JTextField in = new JTextField(JTextField.CENTER);		
		JButton sim = new JButton("Simulate");
		JButton clr = new JButton("Clear");
		
		in.setHorizontalAlignment(SwingConstants.CENTER);
		
		sim.setPreferredSize(new Dimension(250, 28));
		clr.setPreferredSize(new Dimension(250, 28));
		
		//Board JPanel
		board.add(comp, BorderLayout.CENTER);
		board.setPreferredSize(new Dimension(500, 500));
		
		//Input JPanel
		input.add(status);
		input.add(in);
		input.add(clr);
		input.add(sim);
		input.setPreferredSize(new Dimension(500, 75));
		
		//Add components
		frame.add(board, BorderLayout.CENTER);
		frame.add(input, BorderLayout.PAGE_END);
		
		//JFrame settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//Textbox listener
		in.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = in.getText();
				simulate(input, status, comp);
			}
		});
		
		//Button listeners
		//Clear button
		clr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status.setText("Input a number, then click simulate");
				in.setText("");
				comp.clearNeedles();
			}
		});
		
		//Simulate button
		sim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = in.getText();
				simulate(input, status, comp);
			}
		});
	}
}
