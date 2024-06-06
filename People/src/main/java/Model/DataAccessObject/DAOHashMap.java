package Model.DataAccessObject;

import Model.Class.Person;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the IDAO interface and completes the function code 
 * blocks so that they can operate with a HashMap structure. The NIF is used as 
 * the key.
 * @author Francesc Perez 
 * @version 1.1.0
 */
public class DAOHashMap implements IDAO{
    
    HashMap <String, Person> people = new HashMap();
    
    @Override
    public ArrayList<Person> readAll(){
        return new ArrayList<>(people.values());
    }

    @Override
    public Person read(Person p) {
        Person r = null;
        if(people.containsKey(p.getNif()))
            r = people.get(p.getNif());
        return r;
    }
    
    @Override
    public int insert(Person p) {
        return people.put(p.getNif(),p) !=null ? 1 : 0;
    }

    @Override
    public int update(Person p) {
        return people.put(p.getNif(),p) != null ? 1 : 0;
    }

    @Override
    public int delete(Person p) {
        return people.remove(p.getNif()) != null ? 1: 0;
    }
    
}
