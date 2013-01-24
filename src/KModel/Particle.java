/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.*;

/**
 * A Particle is assumed to be spherical. The particle's mass
 * is assumed to be proportional to r^3. Vector p is the center
 * of the particle's enclosing square, and Vector v contains
 * the particle's velocity components.
 *
 * @author John B. Matthews
 */
class Particle extends Object {

    private double radius = 0;
    private double mass = 0;
    private Color color = new Color(0, true);
    private Image image = null;
    private Vector p = new Vector();
    private Vector v = new Vector();

    /**
     * Construct a dimensionless, massless, invisible,
     * stationary particle at the origin.
     */
    public Particle() {
    }

    /** Return a new Vector with this particle's position. */
    public Vector getPosition() {
        return new Vector(p);
    }
    
    /** Return the given Vector set to this particle's position. */
    public Vector getPosition(Vector p) {
        p.x = this.p.x; p.y = this.p.y;
        return p;
    }
    
    /** Return this particle's x position. */
    public double getX() { return this.p.x; }

    /** Return this particle's y position. */
    public double getY() { return this.p.y; }

    /** Set this particle's position to the given Vector. */
    public void setPosition(Vector p) {
        this.p.x = p.x;
        this.p.y = p.y;
    }

    /** Set this particle's x, y position. */
    public void setPosition(double x, double y) {
        this.p.x = x;
        this.p.y = y;
    }

    /** Return a new Vector with this particle's velocity. */
    public Vector getVelocity() {
        return new Vector(v);
    }
    
    /** Return the given Vector set to this particle's velocity. */
    public Vector getVelocity(Vector v) {
        v.x = this.v.x; v.y = this.v.y;
        return v;
    }
    
    /** Return this particle's x velocity component. */
    public double getVx() { return this.v.x; }

    /** Return this particle's y velocity component. */
    public double getVy() { return this.v.y; }

    /** Return this particle's velocity magnitude. */
    public double getVNorm() {
        return v.norm();
    }

    /** Set this particle's velocity to the given Vector. */
    public void setVelocity(Vector v) {
        this.v.x = v.x; this.v.y = v.y;
    }

    /** Set this particle's x, y velocity components. */
    public void setVelocity(double vx, double vy) {
        this.v.x = vx; this.v.y = vy;
    }
    
    /** Set this particle's radius and imputed mass. */
    public void setR(double radius) {
        this.radius = radius;
        this.mass = radius * radius * radius;
    }

    /** Get this particle's radius. */
    public double getR() { return this.radius; }

    /** Set this particle's imputed mass. */
    public double getM() { return this.mass; }

    /** Get this particle's Color. */
    public Color getColor() { return this.color; }

    /** Set this particle's Color. */
    public void setColor(Color color) { this.color = color; }

    /** Set this particle's Image. */
    public Image getImage() { return image; }

    /** Set this particle's Image. */
    public void setImage(Image image) { this.image = image; }
}
