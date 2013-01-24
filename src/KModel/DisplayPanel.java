/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;

/**
 * DisplayPanel is a view of an Ensemble of Particles.
 * 
 * @author John B. Matthews
 */
class DisplayPanel extends JPanel implements Observer {

    private static final int ROWS = 20;
    private static final int COLS = ROWS;
    private static final Random random = new Random();
    private final Ensemble model;
    private Rectangle2D.Double r = new Rectangle2D.Double();
    private BufferedImage image;
    private TexturePaint paint;
    private boolean useGradient = true;
    private long paintTime;
    private int[] iArray = { 0, 0, 0, 255 };

    /** Construct a display panel. */
    public DisplayPanel(final Ensemble model) {
        this.model = model;
        this.setPreferredSize(new Dimension(700, 600));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            // Handle resize of window. Changes the model's walls.
            public void componentResized(ComponentEvent e) {
            	
            	System.out.println("resized");
                Component c = (Component) e.getSource();
                model.setWalls(COLS, ROWS,
                    c.getWidth() - COLS, c.getHeight() - ROWS);
            }
        });
    }

    /** Paint the display. */
    @Override
    protected void paintComponent(Graphics g) {

    	// Initialize image and model first time.
        if (image == null) initImage();
        if (model.getAtoms().isEmpty()) model.initAtoms();
        // Get current time.
        

    	long start = System.currentTimeMillis();
/*        
        // Draw an image of size ROWS x COLS. This is used
        // for the texture paint of border.
        WritableRaster raster = image.getRaster();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0 ; col < COLS; col++) {
                int v = random.nextInt(256);
                iArray[0] = 200; iArray[1] = v; iArray[2] = 0;
                raster.setPixel(col, row, iArray);
            }
        }
        
*/        
    	// Paint the component's background black.
        g.setColor(Color.black);
        int w = getWidth();
        int h = getHeight();
        g.fillRect(0, 0, w, h);
        // Paint the component's borders with the textured paint.
        Graphics2D g2D = (Graphics2D) g;
        
        // Paint a circle in upper-right and lower-left corners.
        double radius = 20.0;
        double diameter = 2 * radius;
        Ellipse2D ellipse = new Ellipse2D.Double(0, 0, diameter, diameter); 
        Ellipse2D ellipse2 = new Ellipse2D.Double(getWidth()-diameter, getHeight()-diameter, diameter, diameter); 
//        ellipse.setFrame(p1.x - radius, p1.y - radius , diameter, diameter);
        g2D.setPaint(Color.green);
        g2D.fill(ellipse);
        g2D.fill(ellipse2);
        

        /*        

                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setPaint(paint);
        r. setRect(0, 0, getWidth(), ROWS);
        g2D.fill(r);
        r. setRect(0, getHeight() - ROWS, getWidth(), ROWS);
        g2D.fill(r);
        r. setRect(0, 0, COLS, getHeight());
        g2D.fill(r);
        r. setRect(getWidth() - COLS, 0, COLS, getHeight());
        g2D.fill(r);
*/        
        // Make atoms move and then paint them on the component.
        for (Particle atom : model.getAtoms()) {
        	//System.out.println(atom);
        	// Make atom move.
            model.iterate(atom);
            // Get position of atom
            Shape shape = model.getShape(atom);
            // Paint atom
            if (useGradient) {
                Image atomImage = atom.getImage();
                int x = (int) shape.getBounds2D().getX();
                int y = (int) shape.getBounds2D().getY();
                g2D.drawImage(atomImage, x, y, null);
            }
            else {
                g2D.setPaint(atom.getColor());
                g2D.fill(shape);
            }
            
        }
        // Compute time to paint component.
        paintTime = System.currentTimeMillis() - start;
    }

    /** Initialize offscreen buffer and paint. */
    private void initImage() {
        image = (BufferedImage) createImage(COLS, ROWS);
/*        
        r.setRect(0, 0, ROWS, COLS);
        paint = new TexturePaint(image, r);
*/
        
    }

    /** Return time taken in paintComponent. */
    public long getPaintTime() {return paintTime; }

    /** Specify color (true) or gray (false). */
    public void useGradient(boolean state) { useGradient = state; }

    @Override
    public void update(Observable o, Object o1) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if("color".equals(o1)){
            System.out.println("Using color");
            useGradient(false);
        }
        else if("gradient".equals(o1)){
            System.out.println("Using gradient");
            useGradient(true);
        }
        else if(o1 == null){
            repaint();
        }
    }
}
