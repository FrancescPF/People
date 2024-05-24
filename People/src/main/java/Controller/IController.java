package Controller;

import Model.Class.Person;
import java.util.ArrayList;

/**
 *
 * @author Fran Perez
 */
public interface IController {

//    public abstract List<Student> showAllStudents() throws DAO_Excep, Student_Excep;
//    public void deleteAllStudents() throws DAO_Excep, Student_Excep;  
    
    public abstract Person read(Person p);
    public abstract ArrayList<Person> readAll();
    public abstract void insert(Person p);
    public abstract void update(Person p);
    public abstract void delete(Person p);
    public abstract void start();

}
