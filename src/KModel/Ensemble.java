/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * An Ensemble of spherical Particles that undergo elastic
 * collision between Particles and the walls of a container.
 * 
 * @author John B. Matthews
 */
class Ensemble implements Observer {

    private static final Random random = new Random();
    private static final Color[] colors = GradientImage.createColorArray();
    private static final int MAX_ATOMS = colors.length * 10;
    private static final int MIN_RADIUS = 20;
    private static final Image[] images = GradientImage.createImageArray(MIN_RADIUS);
    private static final int VSCALE = 5;
    private int left = 0;
    private int top = 0;
    private int right = 500;
    private int bottom = 500;
    private int iterations = 0;
    private int collisions = 0;
    private ArrayList<Particle> atoms;
    private Ellipse2D ellipse = new Ellipse2D.Double();
    private Vector p1 = new Vector(); // position
    private Vector p2 = new Vector();
    private Vector v1 = new Vector(); // velocity 
    private Vector v2 = new Vector();
    private Vector n  = new Vector(); // normal vector
    private Vector un = new Vector(); // unit normal
    private Vector ut = new Vector(); // unit tangent

    /** Construct a container of atoms. */
    public Ensemble() {
        atoms = new ArrayList<Particle>(MAX_ATOMS);
    }

    /** Set the container boundaries. */
    public void setWalls(int l, int t, int r, int b) {
        left = l;
        top = t;
        right = r;
        bottom = b;
        resetCollisionRate();
    }

    /** Advance each atom. */
    public void iterate(Particle atom1) {
        iterations++;
        p1 = atom1.getPosition(p1);
        v1 = atom1.getVelocity(v1);
        p1.add(v1.scale(VSCALE));
        atom1.setPosition(p1);
/*
        for (int i = atoms.indexOf(atom1) + 1; i < atoms.size(); i++) {
            Particle atom2 = atoms.get(i);
            collideAtoms(atom1, atom2);
        }
        collideWalls(atom1);
*/        
    }

    /** Get an atom's shape. */
    public Shape getShape(Particle atom) {
        double radius = atom.getR();
        double diameter = 2 * radius;
        p1 = atom.getPosition(p1);
        ellipse.setFrame(p1.x - radius, p1.y - radius , diameter, diameter);
        return ellipse;
    }

    // Check for collision between atom1 & atom2
    private void collideAtoms(Particle a1, Particle a2) {
        double radius = a1.getR() + a2.getR();
        p1 = a1.getPosition(p1);
        p2 = a2.getPosition(p2);
        n = n.set(p1).subtract(p2);
        if (n.norm() < radius) {
            // Move to start of collision
            double dr = (radius - n.norm()) / 2;
            un = un.set(n).unitVector();
            p1.add(un.scale(dr));
            un = un.set(n).unitVector();
            p2.add(un.scale(-dr));
            a1.setPosition(p1);
            a2.setPosition(p2);
            // Find normal and tangential components of v1/v2
            n = n.set(p1).subtract(p2);
            un = un.set(n).unitVector();
            ut = ut.set(-un.y, un.x);
            v1 = a1.getVelocity(v1);
            v2 = a2.getVelocity(v2);            
            double v1n = un.dot(v1);
            double v1t = ut.dot(v1);
            double v2n = un.dot(v2);
            double v2t = ut.dot(v2);
            // Calculate new v1/v2 in normal direction
            double m1 = a1.getM();
            double m2 = a2.getM();
	        double v1nNew = (v1n * (m1 - m2) + 2d * m2 * v2n) / (m1 + m2);
	        double v2nNew = (v2n * (m2 - m1) + 2d * m1 * v1n) / (m1 + m2);
	        // Update velocities with sum of normal & tangential components
	        v1 = v1.set(un).scale(v1nNew);
	        v2 = v2.set(ut).scale(v1t);
            a1.setVelocity(v1.add(v2));
	        v1 = v1.set(un).scale(v2nNew);
	        v2 = v2.set(ut).scale(v2t);
            a2.setVelocity(v1.add(v2));
        }
    }

    // Check for collision with wall
    private void collideWalls(Particle atom) {
        double radius = atom.getR();
        p1 = atom.getPosition(p1);
        v1 = atom.getVelocity(v1);
        if (p1.x < left + radius) {
            p1.x = left + radius;
            v1.x = -v1.x;
            collisions++;
        }
        if (p1.y < top + radius) {
            p1.y = top + radius;
            v1.y = -v1.y;
            collisions++;
        }
        if (p1.x > right - radius) {
            p1.x = right - radius;
            v1.x = -v1.x;
            collisions++;
        }
        if (p1.y > bottom - radius) {
            p1.y = bottom - radius;
            v1.y = -v1.y;
            collisions++;
        }
        atom.setPosition(p1);
        atom.setVelocity(v1);
    }
    
    /** Add atoms to half of max. */
    public void initAtoms() {
        atoms.clear();
        
        for (int i = 0; i < 3; i++) {
            addAtoms();
        }
        
/*
        for (int i = 0; i < MAX_ATOMS / colors.length / 2; i++) {
            addAtoms();
        }
        resetCollisionRate();
*/    }

    /** Add one atom of each color. */
    public void addAtoms() {
        if (atoms.size() >= MAX_ATOMS) return;

        Particle atom = new Particle();
        atom.setPosition(
            random.nextDouble() * (right - left) + left,
            random.nextDouble() * (bottom - top) + top);
        int id = atoms.size() % colors.length;
        atom.setColor(colors[id]);
        atom.setImage(images[id]);
        atom.setR(id + MIN_RADIUS);
        double vx = random.nextDouble();
        if (random.nextBoolean()) vx = -vx;
        double vy = random.nextDouble();
        if (random.nextBoolean()) vy = -vy;
        atom.setVelocity(vx, vy);
        atoms.add(atom);
/*
        for (int i = 0; i < colors.length && atoms.size() <= MAX_ATOMS; i++) {
            Particle atom = new Particle();
            atom.setPosition(
                random.nextDouble() * (right - left) + left,
                random.nextDouble() * (bottom - top) + top);
            int id = i % colors.length;
            atom.setColor(colors[id]);
            atom.setImage(images[id]);
            atom.setR(id + MIN_RADIUS);
            double vx = random.nextDouble();
            if (random.nextBoolean()) vx = -vx;
            double vy = random.nextDouble();
            if (random.nextBoolean()) vy = -vy;
            atom.setVelocity(vx, vy);
            atoms.add(atom);
        }
*/        
    }

    /** Remove one atom of each color. */
    public void removeAtoms() {
/*
    	for (int i = 0; i < colors.length && atoms.size() > 0; i++)
            atoms.remove(0);
*/
    	if( atoms.size() > 0 ) atoms.remove( atoms.size()-1 );
    }

    /** Get the ratio of wall collisions to iterations. */
    public double getCollisionRate() {
        if (iterations == 0) return 0;
        double rate = 4d * collisions / iterations;
        return Math.min(rate, 1d);
    }

    /** Get the list of atoms. */
    public ArrayList<Particle> getAtoms() { return atoms; }

    /** Reset the collision counter; called when container resized. */
    private void resetCollisionRate() {
        iterations = 0;
        collisions = 0;
    }

    @Override
    public void update(Observable o, Object o1) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if("reset".equals(o1)){
            initAtoms();
        }
        else if("plus".equals(o1)){
            addAtoms();
        }
        else if("minus".equals(o1)){
            removeAtoms();
        }
    }
}
