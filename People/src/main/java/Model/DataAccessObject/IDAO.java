package Model.DataAccessObject;

import Model.Class.Person;


/**
 *
 * @author fran
 * 
 */

public interface IDAO {
     
//    public abstract List<Student> readALL () throws DAO_Excep;
//        public abstract int deleteALL () throws DAO_Excep;
      
    public abstract Person read (Person p);
    public abstract int insert(Person p);
    public abstract int update (Person p);   
    public abstract int delete (Person p);
   
}
