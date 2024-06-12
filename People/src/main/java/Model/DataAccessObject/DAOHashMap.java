package Model.DataAccessObject;

import Model.Class.Person;
import Model.Class.PersonException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the IDAO interface and completes the function code
 * blocks so that they can operate with a HashMap structure. The NIF is used as
 * the key.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DAOHashMap implements IDAO {

    HashMap<String, Person> people = new HashMap();

    @Override
    public Person read(Person p){
        return people.containsKey(p.getNif()) ? people.get(p.getNif()) : null;
    }
    
    @Override
    public void insert(Person p) {
        people.put(p.getNif(), p);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public ArrayList<Person> readAll() throws PersonException {
        if (people.isEmpty()) {
            throw new PersonException("There aren't people registered "
                    + "yet.");
        }
        return new ArrayList<>(people.values());
    }



    @Override
    public void update(Person p) throws PersonException {
        if (people.replace(p.getNif(), p) == null) {
            throw new PersonException(p.getNif() + " is not registered and can "
                    + "not be UPDATED");
        }
    }

    @Override
    public void delete(Person p) throws PersonException {
        if (people.remove(p.getNif()) == null) {
            throw new PersonException(p.getNif() + " is not registered and can "
                    + "not be DELETED");
        }
    }

}
