package Model.DataAccessObject;

import Model.Class.Person;
import Model.Class.PersonException;
import java.util.ArrayList;

/**
 * This class implements the IDAO interface and completes the code blocks of 
 * the functions so that they can operate with an ArrayList structure. Thanks 
 * to the overriding of the "equals" method in the Person class, the ArrayList 
 * will not be able to contain objects with the same NIF.
 * @author Francesc Perez 
 * @version 1.1.0
 */
public class DAOArrayList implements IDAO{
    
    ArrayList <Person> people = new ArrayList<>();

    @Override
    public Person read(Person p){
        return people.contains(p) ? people.get(people.indexOf(p)) : null;
    }
    
    @Override
    public void insert(Person p) {
        people.add(p);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public ArrayList<Person> readAll() throws PersonException{
        if(people.isEmpty())
            throw new PersonException("There aren't people registered "
                    + "yet.");
        return people;
    }
    
    

    @Override
    public void update(Person p) throws PersonException{
        try{
            people.set(people.indexOf(p), p);
        }catch(IndexOutOfBoundsException ex){
            throw new PersonException(p.getNif() + " is not registered and can "
                    + "not be UPDATED");
        }
    }

    @Override
    public void delete(Person p) throws PersonException {
        if(!people.remove(p)){
            throw new PersonException(p.getNif() + " is not registered and can "
                    + "not be DELETED");
        }
    }
    
}
