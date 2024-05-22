package Model.DataAccessObject;

import Model.Class.Person;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DAOFileSerializable implements IDAO {

    String sep = File.separator;
    String projectPath = System.getProperty("user.dir");
    String folderPath = projectPath + sep + "People";
    File folderProject = new File(folderPath);
    String folderPhotoPath = folderPath + sep + "Photos";
    File folderPhotoProject = new File(folderPhotoPath);
    String filePath = folderPath + sep + "PeopleSerializable.dat";
    File fileProject = new File(filePath);

    @Override
    public Person read(Person p) {
        Person personToReturn = null;
        if (fileProject.exists()) {
            System.out.println("READ");
            ObjectInputStream ois = null;
            FileInputStream fIS = null;
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
            } catch (FileNotFoundException ex) {
                System.out.println("Fichero no encontrado");
            } catch (EOFException ex) {
                System.out.println("Fin fichero");
            } catch (ClassNotFoundException ex) {
                System.out.println("Clase no encontrada");
            } catch (IOException ex) {
                System.out.println("Algo pasa con el fichero READ");
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                        fIS.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Algo pasa con el archivo");
                }
            }
        }
        return personToReturn;
    }

    @Override
    public int insert(Person p) {
        //NO funciona el append = true con la serialización
        //Leeré todo el archivo, cargaré los objetos en un ArrayList 
        //y sobreescribiré el fichero
        ArrayList<Person> personRead = new ArrayList<>();

        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS = null;
            try {
                fIS = new FileInputStream(fileProject);
                ois = new ObjectInputStream(fIS);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    personRead.add(pr);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Fichero no encontrado");
            } catch (EOFException ex) {
                System.out.println("Fin fichero");
            } catch (ClassNotFoundException ex) {
                System.out.println("Clase no encontrada");
            } catch (IOException ex) {
                System.out.println("Algo pasa con el fichero READ");
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                        fIS.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Algo pasa con el archivo");
                }
            }

            //Añado al ArrayList la nueva persona y sobreescribo el fichero
            ObjectOutputStream oos = null;
            FileOutputStream fOS = null;
            try {

                fOS = new FileOutputStream(fileProject);
                oos = new ObjectOutputStream(fOS);
                personRead.add(p);
                for (Person pf : personRead) {
                    oos.writeObject(pf);
                }
            } catch (IOException ex) {
                System.out.println("Algo pasa con el fichero");
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                        fOS.close();
                        return 1;
                    }
                } catch (IOException ex) {
                    System.out.println("Algo pasa con el fichero");
                }
            }
        }
        return 0;
    }

    @Override
    public int update(Person p) {
        //NO funciona el append = true con la serialización
        //Leeré todo el archivo, cargaré los objetos en un ArrayList 
        //y sobreescribiré el fichero
        ArrayList<Person> personRead = new ArrayList<>();
        if (fileProject.exists()) {
            ObjectInputStream ois = null;
            FileInputStream fIS = null;
            try {
                fIS = new FileInputStream(fileProject);
                ois = new ObjectInputStream(fIS);
                Person pr;
                while ((pr = (Person) ois.readObject()) != null) {
                    personRead.add(pr);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Fichero no encontrado");
            } catch (EOFException ex) {
                System.out.println("Fin fichero");
            } catch (ClassNotFoundException ex) {
                System.out.println("Clase no encontrada");
            } catch (IOException ex) {
                System.out.println("Algo pasa con el fichero READ");
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                        fIS.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Algo pasa con el archivo");
                }
            }
            personRead.set(personRead.indexOf(new Person(p.getNif())), p);
            ObjectOutputStream oos = null;
            FileOutputStream fOS = null;
            try {
                fOS = new FileOutputStream(fileProject);
                oos = new ObjectOutputStream(fOS);
                personRead.add(p);
                for (Person pf : personRead) {
                    oos.writeObject(pf);
                }
            } catch (IOException ex) {
                System.out.println("Algo pasa con el fichero");
            } finally {
                try {
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                        fOS.close();
                        return 1;
                    }
                } catch (IOException ex) {
                    System.out.println("Algo pasa con el fichero");
                }
            }
        }
        return 1;
    }

        @Override
        public int delete
        (Person p
        
            ) {
//        RandomAccessFile rafRW = null;
//        String foundPerson = null;
//        try {
//            rafRW = new RandomAccessFile(filePath, "rw");
//            System.out.println(rafRW);
//            String textoNuevo = "";
//            while (rafRW.getFilePointer() < rafRW.length()) {
//                String l = rafRW.readLine();
//                String d[] = l.split("\t");
//                if (p.getNif().equals(d[1])) {
//                    System.out.println("Persona a actualizar encontrada");
//                    foundPerson = d[3];
//                } else {
//                    textoNuevo += d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\n";
//                }
//            }
//            //Vacio el archivo
//            rafRW.setLength(0);
//            //Escribo el archivo sin el usuario a actualizar
//            System.out.println(textoNuevo);
//            rafRW.writeBytes(textoNuevo);
////            rafRW.writeUTF(textoNuevo);
//        } catch (FileNotFoundException ex) {
//            System.out.println("Algo pasa abriendo el archivo");
//            return -1;
//        } catch (IOException ex) {
//            System.out.println("No se puede leer el archivo");
//            return -1;
//        } finally {
//            if (rafRW != null) {
//                try {
//                    rafRW.close();
//                    if (foundPerson != null) {
//                        File photoFile = new File(folderPhotoPath + sep + p.getNif() + ".png");
//                        System.out.println(photoFile.getAbsolutePath());
//                        photoFile.delete();
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(RandomAccess.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
        return 1;
        }

    }
