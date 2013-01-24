/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.*;
import javax.swing.*;

/**  
 * KineticModel: A program that models elastic collisions.
 *
 * @author John B. Matthews
 */
public class KineticModel extends JFrame {

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KineticModel();
            }
        });
    }

    /** Construct a KineticModel frame. */
    public KineticModel() {
        this.setTitle("KineticModel");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(kineticModelPanel(this));
        this.pack();
        this.setVisible(true);
    }

    /**
     * Return a KineticModel panel.
     * The optional frame parameter is used for the enclosing
     * JFrame's default button. A null frame is ignored.
     * 
     * @param frame the enclosing JFrame
     */
    public static JPanel kineticModelPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Ensemble model = new Ensemble();
        DisplayPanel view = new DisplayPanel(model);
        panel.add(view, BorderLayout.CENTER);
        ControlPanel controls = new ControlPanel(view, model);
        panel.add(controls, BorderLayout.EAST);
        if (frame != null) {
            frame.getRootPane().setDefaultButton(
                controls.getDefaultButton());
        }
        return panel;
    }
}
