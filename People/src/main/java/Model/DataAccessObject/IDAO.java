package Model.DataAccessObject;

import Model.Class.Person;
import java.util.ArrayList;

/**
 * This interface defines the operations that will have to be performed on the 
 * data storage system chosen by the user. Depending on the system, the 
 * implementation varies.
 * @author Francesc Perez 
 * @version 1.0
 */

public interface IDAO { 
    public abstract Person read (Person p);
    public abstract ArrayList<Person> readAll();
    public abstract int insert(Person p);
    public abstract int update (Person p);   
    public abstract int delete (Person p);
}
