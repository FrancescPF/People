package Model.DataAccessObject;

import Model.Class.Person;
import java.util.ArrayList;

/**
 *
 * @author Fran Perez
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
