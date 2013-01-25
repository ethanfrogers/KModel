/** Distributed under the terms of the GPL, version 3. */
package KModel;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.*;

/**
 * ControlPanel.
 * 
 * @author John B. Matthews
 */
class ControlPanel extends JPanel
    implements ActionListener, ChangeListener, Observer {

    private static final int RATE = 25; // 25 Hz
    private static final int STRUT = 8;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final DecimalFormat pf = new DecimalFormat("0%");
    private DisplayPanel view;
    private Ensemble model;
    private Histogram histogram;
    private JButton runButton = new JButton();
    private JButton resetButton = new JButton();
    private JButton plusButton = new JButton();
    private JButton minusButton = new JButton();
    private JLabel paintLabel = new JLabel();
    private JLabel countLabel = new JLabel();
    private JLabel collisionLabel = new JLabel();
    private JSpinner spinner = new JSpinner();
    private JLabel histLabel = new JLabel();
    private ControlSubject subject = new ControlSubject();
    
    // Create timer that controls animation. The timer
    // fires an event every 1000/RATE milliseconds. The 
    // event handler is "this" (see actionPerformed event
    // handler below). The timer's rate can be changed by 
    // a Spinner control (see stateChanged event handler below).
    private Timer timer = new Timer(1000/RATE, this);

    /** Construct a control panel. */
    public ControlPanel(DisplayPanel view, Ensemble model) {
    	// Store the model and view.
        subject.addObserver(model);
        subject.addObserver(view);
        this.view = view;
        this.model = model;
        histogram = new Histogram(model.getAtoms());
        timer.setInitialDelay(200);
        // Add resize event handler.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Catch a breath while resizing.
                if (timer.isRunning()) timer.restart();
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        runButton.setText("Run");
        runButton.setActionCommand("run");
        runButton.addActionListener(this);
        panel.add(runButton);

        resetButton.setText("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        panel.add(resetButton);

        plusButton.setText("Atoms +");
        plusButton.setActionCommand("plus");
        plusButton.addActionListener(this);
        panel.add(plusButton);

        minusButton.setText("Atoms -");
        minusButton.setActionCommand("minus");
        minusButton.addActionListener(this);
        panel.add(minusButton);

        panel.add(Box.createVerticalStrut(STRUT));
        JRadioButton colorButton = new JRadioButton("Color");
        colorButton.setMnemonic(KeyEvent.VK_C);
        colorButton.setActionCommand("color");
        colorButton.setSelected(false);
        panel.add(colorButton);

        JRadioButton grayButton = new JRadioButton("Gradient");
        grayButton.setMnemonic(KeyEvent.VK_G);
        grayButton.setActionCommand("gradient");
        grayButton.setSelected(true);
        panel.add(grayButton);

        ButtonGroup group = new ButtonGroup();
        group.add(colorButton);
        group.add(grayButton);
        colorButton.addActionListener(this);
        grayButton.addActionListener(this);
        
        panel.add(Box.createVerticalStrut(STRUT));
        panel.add(paintLabel);
        panel.add(countLabel);
        panel.add(collisionLabel);

        panel.add(Box.createVerticalStrut(STRUT));
        JLabel rateLabel = new JLabel("Update (Hz):");
        panel.add(rateLabel);
        // Set spinner to return a value between 5 and 50.
        // This controls the timer interval which ranges from
        // 0.02 sec (1000/50 msec) to 0.2 sec (1000/5 msec). 
        spinner.setModel(new SpinnerNumberModel(RATE, 5, 50, 5));
        spinner.addChangeListener(this);
        spinner.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
        panel.add(spinner);

        panel.add(Box.createVerticalStrut(STRUT));
        panel.add(histLabel);
        panel.add(histogram);

        this.add(panel);
        // Call to start timer.
        toggle();
    }

    /** Return the defualt button. 
     * Allows the Enter key to press the Run/Stop buttton*/
    public JButton getDefaultButton() {
        return runButton;
    }
    /** Called when Run/Stop button is pressed.
     */
    private void toggle() {
        if (timer.isRunning()) {
            timer.stop();
            runButton.setText("Start");
        } else {
            timer.start();
            runButton.setText("Stop");
        }
    }

    /** Handle buttons and timer. */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String cmd = e.getActionCommand();
        
        // If timer event is fired, do the following... 
        if (source == timer && cmd == null) {
        
        	// Repaint the view. PaintComponent is called in the view
        	// which makes the atoms move, collide with each other, 
        	// and the walls.
                passAlong(cmd);
        	//view.repaint();
        	// Display the time required to repaint in milliseconds
           long pt = view.getPaintTime();
           if (pt < 2) paintLabel.setText("Paint: ~1");
           else paintLabel.setText("Paint: " + view.getPaintTime());
           // Display some model statistics
           countLabel.setText("Atoms: " + model.getAtoms().size());
           collisionLabel.setText("Collide: " + pf.format(model.getCollisionRate()));
           histLabel.setText("Velocity: " + df.format(histogram.getAverage()));
           histogram.repaint();
           
           // Handle button events
           
           // If run button is pressed, toggle the timer
        } else if ("run".equals(cmd)) {
           toggle();
           
        }else{
            // If reset button is pressed, restart the animation
            if("reset".equals(cmd))
                timer.restart();
            //Notify all observers of a button press
            passAlong(cmd);
        }
        
    }
    /**
     * method to set the observable cmd string. 
     * @param str command to set in the subject
     */
    public void passAlong(String str){
        subject.setCmd(str);
    }

    /** Handle spinners. */
    public void stateChanged(ChangeEvent e) {
    	System.out.println("Spinner state change");
        Object source = e.getSource();
        if (source == spinner) {
            int rate = ((Number) spinner.getValue()).intValue();
            timer.setDelay(1000 / rate);
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
