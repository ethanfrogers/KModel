/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package KModel;

import java.util.Observable;

/**
 *
 * @author efrogers_it
 */
public class ControlSubject extends Observable {
    
    String cmd;
    
    public ControlSubject()
    {
        cmd = null;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
        this.setChanged();
        this.notifyObservers(cmd);
    }
    
    
    
}
