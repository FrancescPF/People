package Model.DataAccessObject;

import Model.Class.Person;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.RandomAccess;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DAOFile implements IDAO {

    String sep = File.separator;
    String projectPath = System.getProperty("user.dir");
    String folderPath = projectPath + sep + "PeopleFile";
    File folderProject = new File(folderPath);
    String folderPhotoPath = folderPath + sep + "PhotosFile";
    File folderPhotoProject = new File(folderPhotoPath);
    String filePath = folderPath + sep + "PeopleFile.txt";
    File fileProject = new File(filePath);
    
    
    @Override
    public ArrayList<Person> readAll(){
        ArrayList<Person> people = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileProject);
            br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            while (line != null) {
                String data[] = line.split("\t");
                String dni = data[0];
                String name = data[1];
                Date date = null;
                if (!data[2].equals("null")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    date = dateFormat.parse(data[2]);
                }
                ImageIcon photo = null;
                if (!data[3].equals("null")) {
                    photo = new ImageIcon(folderPhotoPath + sep + data[3] + ".png");
                }
                people.add(new Person(data[0], data[1], date, photo));
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Project file not found");
        } catch (IOException ex) {
            System.out.println("Can access project file");
        } catch (ParseException ex) {
            System.out.println("Can not convert Date");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(DAOFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return people;
    }

    @Override
    public Person read(Person p) {
        Person personToReturn = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileProject);
            br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            while (line != null) {
                String data[] = line.split("\t");
                System.out.println(Arrays.toString(data));
                if (data[1].equals(p.getNif())) {
                    Date date = null;
                    if (!data[2].equals("null")) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        date = dateFormat.parse(data[2]);
                    }
                    ImageIcon photo = null;
                    if (!data[3].equals("null")) {
                        photo = new ImageIcon(folderPhotoPath + sep + data[3] + ".png");
                    }
                    personToReturn = new Person(data[0], data[1], date, photo);
                    break;
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Project file not found");
        } catch (IOException ex) {
            System.out.println("Can access project file");
        } catch (ParseException ex) {
            System.out.println("Can not convert Date");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(DAOFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println(personToReturn);
        return personToReturn;
    }

    @Override
    public int insert(Person p) {
        int returnValue = 0;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fileProject, true);
            bw = new BufferedWriter(fw);
            if (p.getDateOfBirth() != null) {
                DateFormat formato = new SimpleDateFormat("yyy/MM/dd");
                String fechaComoString = formato.format(p.getDateOfBirth());
                bw.write(p.getName() + "\t" + p.getNif() + "\t" + fechaComoString + "\t");
            } else {
                bw.write(p.getName() + "\t" + p.getNif() + "\t" + "null" + "\t");
            }
            if (p.getPhoto() != null) {
                FileOutputStream out = null;
                BufferedOutputStream outB = null;
                try {
                    out = new FileOutputStream(folderPhotoProject + sep + p.getNif() + ".png");
                    outB = new BufferedOutputStream(out);
                    BufferedImage bi = new BufferedImage(p.getPhoto().getImage().getWidth(null),
                            p.getPhoto().getImage().getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);
                    bi.getGraphics().drawImage(p.getPhoto().getImage(), 0, 0, null);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bi, "png", baos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] img = baos.toByteArray();
                    for (int i = 0; i < img.length; i++) {
                        outB.write(img[i]);
                    }
                } finally {
                    if (outB != null) {
                        outB.close();
                    }
                }
                bw.write(p.getNif() + "\n");
            } else {
                bw.write("null" + "\n");
            }
            returnValue = 1;
        } catch (IOException ex) {
            System.out.println("Error accesing fileProject");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(DAOFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return returnValue;
    }

    @Override
    public int update(Person p) {
        RandomAccessFile rafRW = null;
        try {
            rafRW = new RandomAccessFile(filePath, "rw");
            System.out.println(rafRW);
            String textoNuevo = "";
            while (rafRW.getFilePointer() < rafRW.length()) {
                String l = rafRW.readLine();
                String d[] = l.split("\t");
                if (p.getNif().equals(d[1])) {
                    System.out.println("Persona a actualizar encontrada");
                } else {
                    textoNuevo += d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\n";
                }
            }
            //Vacio el archivo
            rafRW.setLength(0);
            //Escribo el archivo sin el usuario a actualizar
            System.out.println(textoNuevo);
            rafRW.writeBytes(textoNuevo);
//            rafRW.writeUTF(textoNuevo);
        } catch (FileNotFoundException ex) {
            System.out.println("Algo pasa abriendo el archivo");
            return -1;
        } catch (IOException ex) {
            System.out.println("No se puede leer el archivo");
            return -1;
        } finally {
            if (rafRW != null) {
                try {
                    rafRW.close();
                    //Inserto el usuario nuevo con las modificaciones
                    insert(p);
                } catch (IOException ex) {
                    Logger.getLogger(RandomAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 1;
    }

    @Override
    public int delete(Person p) {
        RandomAccessFile rafRW = null;
        String foundPerson = null;
        try {
            rafRW = new RandomAccessFile(filePath, "rw");
            System.out.println(rafRW);
            String textoNuevo = "";
            while (rafRW.getFilePointer() < rafRW.length()) {
                String l = rafRW.readLine();
                String d[] = l.split("\t");
                if (p.getNif().equals(d[1])) {
                    System.out.println("Persona a actualizar encontrada");
                    foundPerson = d[3];
                } else {
                    textoNuevo += d[0] + "\t" + d[1] + "\t" + d[2] + "\t" + d[3] + "\n";
                }
            }
            //Vacio el archivo
            rafRW.setLength(0);
            //Escribo el archivo sin el usuario a actualizar
            System.out.println(textoNuevo);
            rafRW.writeBytes(textoNuevo);
//            rafRW.writeUTF(textoNuevo);
        } catch (FileNotFoundException ex) {
            System.out.println("Algo pasa abriendo el archivo");
            return -1;
        } catch (IOException ex) {
            System.out.println("No se puede leer el archivo");
            return -1;
        } finally {
            if (rafRW != null) {
                try {
                    rafRW.close();
                    if(foundPerson != null){
                        File photoFile = new File(folderPhotoPath + sep + p.getNif() + ".png");
                        System.out.println(photoFile.getAbsolutePath());
                        photoFile.delete();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(RandomAccess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 1;
    }

}
