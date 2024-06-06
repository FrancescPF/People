package Model.DataAccessObject;

import Model.Class.Person;
import java.util.ArrayList;

/**
 * This class implements the IDAO interface and completes the code blocks of 
 * the functions so that they can operate with an ArrayList structure. Thanks 
 * to the overriding of the equals method in the Person class, the ArrayList 
 * will not be able to contain objects with the same NIF.
 * @author Francesc Perez 
 * @version 1.1.0
 */
public class DAOArrayList implements IDAO{
    
    ArrayList <Person> people = new ArrayList<>();

    @Override
    public Person read(Person p) {
        Person r = null;
        if(people.contains(p))
            r = people.get(people.indexOf(p));
        return r;
    }
    
    @Override
    public ArrayList<Person> readAll(){
        return people;
    }
    
    @Override
    public int insert(Person p) {
        return people.add(p) ? 1 : 0;
    }

    @Override
    public int update(Person p) {
        return people.set(people.indexOf(p), p) != null ? 1 : 0;
    }

    @Override
    public int delete(Person p) {
        return people.remove(p) ? 1: 0;
    }
    
}
