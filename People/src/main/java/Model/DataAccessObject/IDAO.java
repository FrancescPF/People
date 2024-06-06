package Model.DataAccessObject;

import Model.Class.Person;
import java.util.ArrayList;

/**
 * This interface defines the operations that will have to be performed on the 
 * data storage system chosen by the user. Depending on the system, the 
 * implementation varies.
 * @author Francesc Perez 
 * @version 1.0.0
 */
public interface IDAO { 
    /**
     * This function returns, if it exists, the registered person whose NIF 
     * matches the NIF assigned to the person entered as an input argument and 
     * returns, if it does not exist, "null".
     * @param p Person
     * @return Person or null
     */
    public abstract Person read (Person p);
    
    /**
     * This function returns an ArrayList<Person> of all registered persons.
     * If there're not registered person returns an empty ArrayList<Person>
     * @return ArrayList<Person>
     */
    public abstract ArrayList<Person> readAll();
    
    /**
     * This function receives the person who must enter the storage system and 
     * returns a 1 on success or a 0 otherwise.
     * @param p Person
     * @return int
     */
    public abstract int insert(Person p);
    
    /**
     * This function receives the person with the updated data and enters it 
     * into the storage system and returns a 1 on success or a 0 otherwise.
     * @param p Person
     * @return int
     */
    public abstract int update (Person p);   
    
    /**
     * This function receives the person (NIF) and deletes it from the storage 
     * system. Returns a 1 on success or a 0 otherwise.
     * @param p Person
     * @return int
     */
    public abstract int delete (Person p);
}
