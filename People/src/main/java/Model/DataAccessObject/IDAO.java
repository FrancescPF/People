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
    /**
     * This function returns, if it exists, the registered person whose NIF 
     * matches the NIF assigned to the person entered as an input argument and 
     * returns, if it does not exist, "null".
     * @param p
     * @return Person or null
     */
    public abstract Person read (Person p);
    
    /**
     * This function returns an ArrayList<Person> of all registered persons.
     * If there're not registered person returns an empty ArrayList<Person>
     * @return ArrayList<Person>
     */
    public abstract ArrayList<Person> readAll();
    public abstract int insert(Person p);
    public abstract int update (Person p);   
    public abstract int delete (Person p);
}
