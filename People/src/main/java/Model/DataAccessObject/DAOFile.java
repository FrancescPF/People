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

/**
 * This class implements the IDAO interface and completes the code of the 
 * functions so that they can work with files. User data is saved in the 
 * "PeopleFile.txt" file and the associated photos, if any, are saved with the 
 * name NIF.png in the "PhotosFile" folder.
 * @author Fran Perez
 * @version 1.0
 */
public class DAOFile implements IDAO {

    //Variables with information about file locations
    String sep = File.separator;
    String projectPath = System.getProperty("user.dir");
    String folderPath = projectPath + sep + "PeopleFile";
    String folderPhotoPath = folderPath + sep + "PhotosFile";
    File folderPhotoProject = new File(folderPhotoPath);
    String filePath = folderPath + sep + "PeopleFile.txt";
    File fileProject = new File(filePath);
   
    @Override
    public ArrayList<Person> readAll(){
        ArrayList<Person> people = new ArrayList<>();
        FileReader fr;
        BufferedReader br = null;
        try {
            fr = new FileReader(fileProject);
            br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            while (line != null) {
                String data[] = line.split("\t");
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
        } catch (IOException | ParseException ex) {
            //Decide what to do
        } finally {
            if (br != null) {
                try {
                    br.close();
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
        FileReader fr;
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
        } catch (IOException | ParseException ex) {
            //Decide what to do
        }
         finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    //Decide what to do
                }
            }
        }
        return personToReturn;
    }

    @Override
    public int insert(Person p) {
        int returnValue = 0;
        FileWriter fw;
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
                FileOutputStream out;
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
                        //Decide what to do
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
            //Decide what to do
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    //Decide what to do
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
            //Clearing file
            rafRW.setLength(0);
            //Writing the file without the user to update
            System.out.println(textoNuevo);
            rafRW.writeBytes(textoNuevo);
        } catch (FileNotFoundException ex) {
            //Decide what to do
            return 0;
        } catch (IOException ex) {
            //Decide what to do
            return 0;
        } finally {
            if (rafRW != null) {
                try {
                    rafRW.close();
                    //Inserting the new user with the modifications
                    insert(p);
                } catch (IOException ex) {
                    //Decide what to do
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
            //Clearaing file
            rafRW.setLength(0);
            rafRW.writeBytes(textoNuevo);
        } catch (FileNotFoundException ex) {
            //Decide what to do
            return 0;
        } catch (IOException ex) {
            //Decide what to do;
            return 0;
        } finally {
            if (rafRW != null) {
                try {
                    rafRW.close();
                    if(foundPerson != null){
                        File photoFile = new File(folderPhotoPath + sep + p.getNif() + ".png");
                        photoFile.delete();
                    }
                } catch (IOException ex) {
                    //Decide waht to do
                }
            }
        }
        return 1;
    }

}
