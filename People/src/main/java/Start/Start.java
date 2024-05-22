package Start;

import Controller.IController;
import Controller.ControllerImplementation;
import View.DataStorageSelection;

/**
 *
 * @author Fran Perez
 */
public class Start {
    
    public static void main(String[] args) {
        DataStorageSelection dSS = new DataStorageSelection();      
        IController cont = new ControllerImplementation(dSS);
        cont.start();     
     }
}
