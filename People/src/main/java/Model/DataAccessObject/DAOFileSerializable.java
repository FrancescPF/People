package Model.DataAccessObject;

import Model.Class.Person;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class implements the IDAO interface and completes the code of the
 * functions so that they can work with files to store objects. User data is
 * saved in the "PeopleFileS.txt" file and the associated photos, if any, are
 * saved with the name NIF.png in the "PhotosFileS" folder.
 *
 * @author Fran Perez
 * @version 1.0
 */
public class DAOFileSerializable implements IDAO {

    String sep = File.separator;
    String projectPath = System.getProperty("user.dir");
    String folderPath = projectPath + sep + "PeopleFileS";
    String filePath = folderPath + sep + "PeopleFileS.txt";
    File fileProject = new File(filePath);

    @Override
    public ArrayList<Person> readAll() {
        ArrayList<Person> people = new ArrayList<>();
        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS;
            try {
                fIS = new FileInputStream(fileProject);
                System.out.println(fIS);
                ois = new ObjectInputStream(fIS);
                System.out.println(ois);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    people.add(pr);
                }
            } catch (IOException | ClassNotFoundException ex) {
                //Decide what to do
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return people;
    }

    @Override
    public Person read(Person p) {
        Person personToReturn = null;
        if (fileProject.exists()) {
            System.out.println("READ");
            ObjectInputStream ois = null;
            FileInputStream fIS;
            try {
                fIS = new FileInputStream(fileProject);
                System.out.println(fIS);
                ois = new ObjectInputStream(fIS);
                System.out.println(ois);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    System.out.println("READ");
                    System.out.println(p);
                    System.out.println(pr);
                    if (pr.getNif().equals(p.getNif())) {
                        personToReturn = pr;
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                //Decide what to do
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return personToReturn;
    }

    @Override
    public int insert(Person p) {
        ArrayList<Person> personRead = new ArrayList<>();
        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS;
            try {
                fIS = new FileInputStream(fileProject);
                ois = new ObjectInputStream(fIS);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    personRead.add(pr);
                }
            } catch (IOException | ClassNotFoundException ex) {
                //Decide what to do
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
            ObjectOutputStream oos = null;
            FileOutputStream fOS;
            try {
                fOS = new FileOutputStream(fileProject);
                oos = new ObjectOutputStream(fOS);
                personRead.add(p);
                for (Person pf : personRead) {
                    oos.writeObject(pf);
                }
            } catch (IOException ex) {
                //Decide what to do
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                        return 1;
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Person p) {
        ArrayList<Person> personRead = new ArrayList<>();
        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS;
            try {
                fIS = new FileInputStream(fileProject);
                ois = new ObjectInputStream(fIS);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    personRead.add(pr);
                }
            } catch (IOException | ClassNotFoundException ex) {
                //Decide what to do
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
            personRead.set(personRead.indexOf(new Person(p.getNif())), p);
            ObjectOutputStream oos = null;
            FileOutputStream fOS;
            try {
                fOS = new FileOutputStream(fileProject);
                oos = new ObjectOutputStream(fOS);
                personRead.add(p);
                for (Person pf : personRead) {
                    oos.writeObject(pf);
                }
            } catch (IOException ex) {
                //Decide what to do
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                        return 1;
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return 1;
    }

    @Override
    public int delete(Person p) {
        ArrayList<Person> personRead = new ArrayList<>();
        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS;
            try {
                fIS = new FileInputStream(fileProject);
                ois = new ObjectInputStream(fIS);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    personRead.add(pr);
                }
            } catch (IOException | ClassNotFoundException ex) {
                //Decide what to do
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
            personRead.remove(p);
            ObjectOutputStream oos = null;
            FileOutputStream fOS;
            try {
                fOS = new FileOutputStream(fileProject);
                oos = new ObjectOutputStream(fOS);
                personRead.add(p);
                for (Person pf : personRead) {
                    oos.writeObject(pf);
                }
            } catch (IOException ex) {
                //Decide what to do
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                        return 1;
                    }
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return 0;
    }

}
