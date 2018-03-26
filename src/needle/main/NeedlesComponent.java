package needle.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JComponent;

public class NeedlesComponent extends JComponent {

	private static final long serialVersionUID = 6119311963968068166L;

	private static class Needle {
		final double x1;
	    final double y1;
	    final double x2;
	    final double y2;
	    final Color c;

	    public Needle(double x1, double y1, double x2, double y2, Color c) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
	        this.c = c;
	    }   
	}
	
	private final LinkedList<Needle> needles = new LinkedList<Needle>();

	public void addNeedle(double x1, double y1, double x2, double y2) {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();;
		Color randomColor = new Color(r, g, b);
	    needles.add(new Needle(x1, y1, x2, y2, randomColor));        
	}
	
	public void clearNeedles() {
	    needles.clear();
	    repaint();
	}
	
	@Override
	protected void paintComponent(Graphics gr) {
	    super.paintComponent(gr);
	    Graphics2D g2 = (Graphics2D) gr;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.BLACK);
	    g2.drawLine(100, 0, 100, 500);
	    g2.drawLine(200, 0, 200, 500);
	    g2.drawLine(300, 0, 300, 500);
	    g2.drawLine(400, 0, 400, 500);
	    for (int i = 0; i < needles.size(); i++) {
	    	Needle needle = needles.get(i);
	    	Shape n = new Line2D.Double(needle.x1, needle.y1, needle.x2, needle.y2);
	        g2.setColor(needle.c);
	        g2.draw(n);
	    }
	}
	    
}
