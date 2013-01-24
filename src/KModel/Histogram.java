/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * A Histogram of particle velocities.
 * 
 * @author John B. Matthews
 */
class Histogram extends JLabel implements Icon {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 125;
    private static final int SPAN = 4;
    private static final int BINS = WIDTH / SPAN;
    private final int[] bins = new int[BINS];
    private ArrayList<Particle> atoms;
    private double average = 0;

    /** Construct a histogram label. */
    public Histogram(ArrayList<Particle> atoms) {
        this.setIcon(this);
        this.atoms = atoms;
    }

    /**
     * Draw a histogram of velocities at the specified location.
     * This implementation ignores the Component parameter.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (atoms.isEmpty()) return;
        Arrays.fill(bins, 0);
        double vMax = Double.MIN_VALUE;
        double sum = 0;
        for (Particle atom : atoms) {
            double v = atom.getVNorm();
            vMax = Math.max(vMax, v);
            sum += v;
        }
        average = sum / atoms.size();
        int binMax = 0;
        for (Particle atom : atoms) {
            double v = atom.getVNorm();
            int binIndex = (int) (v * (BINS - 1) / vMax);
            bins[binIndex]++;
            binMax = Math.max(binMax, bins[binIndex]);
        }
        g.setColor(Color.black);
        g.fillRect(0, HEIGHT - 1, WIDTH, 1);
        g.setColor(Color.blue);
        for (int i = 0; i < bins.length; i++) {
            int h = (HEIGHT - 4) * bins[i] / binMax;
            g.fillRect(x + i * SPAN, y + HEIGHT - h, SPAN, h);
        }
    }
    
    public int getIconWidth() {
        return WIDTH;
    }

    public int getIconHeight() {
        return HEIGHT;
    }

    /** Set the specified Ensemble of atoms. */
    public void setAtoms(ArrayList<Particle> atoms) {
        this.atoms = atoms;
    }
    
    /** Return the average velocity (rms) of the Ensemble. */
    public double getAverage() {
        return average;
    }
}
