package Controller;

import Model.Class.Person;
import Model.DataAccessObject.DAOArrayList;
import Model.DataAccessObject.DAOFile;
import Model.DataAccessObject.DAOFileSerializable;
import Model.DataAccessObject.DAOHashMap;
import Model.DataAccessObject.DAOSQL;
import Model.DataAccessObject.IDAO;
import View.DataStorageSelection;
import View.Delete;
import View.Insert;
import View.Menu;
import View.Read;
import View.ReadAll;
import View.Update;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.DateModel;

/**
 * This class starts the visual part of the application and programs and manages
 * all the events that it can receive from it. For each event received the
 * controller performs an action.
 *
 * @author Francesc Perez
 * @version 1.0
 */
public class ControllerImplementation implements IController, ActionListener {

    //Instance variables used so that both the visual and model parts can be 
    //accessed from the controller.
    private final DataStorageSelection dSS;
    private IDAO dao;
    private Menu menu;
    private Insert insert;
    private Read read;
    private Delete delete;
    private Update update;
    private ReadAll readAll;

    //Variables for secure connection against the server, DDBB and table.
    private final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private final String JDBC_COMMU_OPT = "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "";
    private final String JDBC_DDBB = "people";
    private final String JDBC_TABLE = "person";
    private final String JDBC_DDBB_TABLE = JDBC_DDBB + "." + JDBC_TABLE;

    /**
     * This constructor allows the controller to know which data storage option
     * the user has chosen. Schedule an event to deploy when the user has made
     * the selection.
     *
     * @param dSS
     * @author Francesc Perez
     * @version 1.0
     */
    public ControllerImplementation(DataStorageSelection dSS) {
        this.dSS = dSS;
        ((JButton) (dSS.getAccept()[0])).addActionListener(this);
    }

    /**
     * With this method, the application is started, asking the user for the
     * chosen storage system.
     *
     * @author Francesc Perez
     * @version 1.0
     */
    @Override
    public void start() {
        dSS.setVisible(true);
    }

//    private StudentShowAll showAllStu;
//    private StudentShowAll deleteAllStu;
//
//    public tControllerImplementation(StudentMenu menuStu, IDAO daoSt) {
//        this.daoSt = daoSt;
//        this.menuStu = menuStu;
//        //JButton objects have defined the method addActionListener, from this 
//        //class we active that when someone press this buttons an ActionListener 
//        //event is launched
//        //With "this" we tell it that it has to execute the action 
//        //defined within the "StudentControllerImplementation" class
//        //in actionPerformed method
//        
//    }
//
//    
//
    private void resetUpdated() {
        update.getNif().setEnabled(true);
        update.getNif().setEditable(true);
        update.getNam().setEnabled(false);
        update.getDateOfBirth().setEnabled(false);
        update.getPhoto().setEnabled(false);
        update.getNam().setText("");
        update.getNif().setText("");
        LocalDate dateLocate = LocalDate.now();
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateLocate.atStartOfDay(systemTimeZone);
        Date dateUtil = java.sql.Date.from(zonedDateTime.toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUtil);
        DateModel<Calendar> dateModel = (DateModel<Calendar>) update.getDateOfBirth().getModel();
        dateModel.setValue(calendar);
        //... but do not display it in the JDatePicker box
        update.getDateOfBirth().getModel().setValue(null);
        update.getPhoto().setIcon(null);
    }

    /**
     * This receives method handles the events of the part. Each event has an
     * associated action.
     *
     * @param e The event generated in the visual part
     * @author Francesc Perez
     * @version 1.0
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Depending on the data storage option selected by the user, the 
        //controller generates a DAO object of one type or another.Through this 
        //object you can do the CRUD of the person entities
        if (e.getSource() == (dSS.getAccept()[0])) {
            String daoSelected = ((javax.swing.JCheckBox) (dSS.getAccept()[1])).getText();
            dSS.dispose();
            if (daoSelected.equals("ArrayList")) {
                dao = new DAOArrayList();
            } else if (daoSelected.equals("HashMap")) {
                dao = new DAOHashMap();
            } else if (daoSelected.equals("File")) {
                String sep = File.separator;
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + sep + "PeopleFile";
                File folderProject = new File(folderPath);
                String folderPhotoPath = folderPath + sep + "PhotosFile";
                File folderPhotoProject = new File(folderPhotoPath);
                String filePath = folderPath + sep + "PeopleFile.txt";
                File fileProject = new File(filePath);
                folderProject.mkdir();
                folderPhotoProject.mkdir();
                if (!fileProject.exists()) {
                    try {
                        fileProject.createNewFile();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dSS, "Could not create files. Closing application.", "File - People v1.0", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                }
                dao = new DAOFile();
            } else if (daoSelected.equals("File (Serialization)")) {
                String sep = File.separator;
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + sep + "PeopleFileS";
                File folderProject = new File(folderPath);
                String folderPhotoPath = folderPath + sep + "PhotosFileS";
                File folderPhotoProject = new File(folderPhotoPath);
                String filePath = folderPath + sep + "PeopleFileS.txt";
                File fileProject = new File(filePath);
               folderProject.mkdir();
                folderPhotoProject.mkdir();
                if (!fileProject.exists()) {
                    try {
                        fileProject.createNewFile();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dSS, "Could not create files. Closing application.", "FileS - People v1.0", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    }
                }
                dao = new DAOFileSerializable();
            } else if (daoSelected.equals("Database")) {
                String sep = File.separator;
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + sep + "PeopleDDBB";
                File folderProject = new File(folderPath);
                String folderPhotoPath = folderPath + sep + "PhotosDDBB";
                File folderPhotoProject = new File(folderPhotoPath);
                folderProject.mkdir();
                folderPhotoProject.mkdir();
                try {
                    Connection conn = null;
                    conn = DriverManager.getConnection(JDBC_URL + JDBC_COMMU_OPT, JDBC_USER, JDBC_PASSWORD);
                    if (conn != null) {
                        String instruction = "create database if not exists " + JDBC_DDBB + ";";
                        Statement stmt = null;
                        stmt = conn.createStatement();
                        stmt.executeUpdate(instruction);
                        stmt.close();
                        String query = "create table if not exists " + JDBC_DDBB + "." + JDBC_TABLE + "("
                                + "nif varchar(9) primary key not null, "
                                + "name varchar(50), "
                                + "dateOfBirth DATE, "
                                + "photo varchar(200) );";
                        stmt = null;
                        stmt = conn.createStatement();
                        stmt.executeUpdate(query);
                        stmt.close();
                        conn.close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dSS, "Something has gone wrong with the database. Closing application.", "DDBB - People v1.0", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                dao = new DAOSQL();
            } else if (daoSelected.equals("Database (Serialization)")) {
                //Has to be done
            }
            dSS.dispose();
            menu = new Menu();
            menu.setVisible(true);
            menu.getInsert().addActionListener(this);
            menu.getRead().addActionListener(this);
            menu.getUpdate().addActionListener(this);
            menu.getDelete().addActionListener(this);
            menu.getReadAll().addActionListener(this);
        } else if (e.getSource()
                == menu.getInsert()) {
            insert = new Insert(menu, true);
            insert.getInsert().addActionListener(this);
            insert.setVisible(true);
        } else if (insert
                != null && e.getSource()
                == insert.getInsert()) {
            Person p = new Person(insert.getNam().getText(), insert.getNif().getText());
            if (insert.getDateOfBirth().getModel().getValue() != null) {
                p.setDateOfBirth(((GregorianCalendar) insert.getDateOfBirth().getModel().getValue()).getTime());
            }
            if ((ImageIcon) insert.getPhoto().getIcon() != null) {
                p.setPhoto((ImageIcon) insert.getPhoto().getIcon());
            }
            insert(p);
            insert.getReset().doClick();
        } else if (e.getSource()
                == menu.getRead()) {
            //Creating SearchStudent JDialog Menu
            read = new Read(menu, true);
            read.getRead().addActionListener(this);
            read.setVisible(true);
        } else if (read
                != null && e.getSource()
                == read.getRead()) {
            read.getNif().setText(read.getNif().getText());
            Person p = new Person(read.getNif().getText());
            Person pNew = read(p);
            if (pNew != null) {
                read.getNam().setText(pNew.getName());
                if (pNew.getDateOfBirth() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(pNew.getDateOfBirth());
                    DateModel<Calendar> dateModel = (DateModel<Calendar>) read.getDateOfBirth().getModel();
                    dateModel.setValue(calendar);
                }
                read.getPhoto().setIcon(pNew.getPhoto());
            } else {
                read.getReset().doClick();
            }
        } else if (e.getSource()
                == menu.getDelete()) {
            delete = new Delete(menu, true);
            delete.getDelete().addActionListener(this);
            delete.setVisible(true);
        } else if (delete
                != null && e.getSource()
                == delete.getDelete()) {
            if (!delete.getNif().getText().isEmpty()) {
                Person p = new Person(delete.getNif().getText());
                delete(p);
                delete.getNif().setText("");
                delete.getNif().setEditable(true);
            } else {
                JOptionPane.showMessageDialog(delete, "Wrong arguments: nif field can not be empty.", "Delete - People v1.0", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource()
                == menu.getUpdate()) {
            update = new Update(menu, true);
            update.getUpdate().addActionListener(this);
            update.getRead().addActionListener(this);
            update.getUpdateReset().addActionListener(this);
            update.setVisible(true);
        } else if (update
                != null && e.getSource()
                == update.getRead()) {
            Person p = new Person(update.getNif().getText());
            Person pNew = read(p);
            if (pNew != null) {
                update.getNam().setText(pNew.getName());
                if (pNew.getDateOfBirth() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(pNew.getDateOfBirth());
                    DateModel<Calendar> dateModel = (DateModel<Calendar>) update.getDateOfBirth().getModel();
                    dateModel.setValue(calendar);
                }
                update.getPhoto().setIcon(pNew.getPhoto());
            } else {
                update.getUpdateReset().doClick();
            }
        } else if (update
                != null && e.getSource()
                == update.getUpdateReset()) {
            resetUpdated();
        } else if (update
                != null && e.getSource()
                == update.getUpdate()) {
            if (!update.getNam().getText().isEmpty()) {
                Person p = new Person(update.getNam().getText(), update.getNif().getText());
                if (update.getDateOfBirth().getModel().getValue() != null) {
                    p.setDateOfBirth(((GregorianCalendar) update.getDateOfBirth().getModel().getValue()).getTime());
                }
                if ((ImageIcon) update.getPhoto().getIcon() != null) {
                    p.setPhoto((ImageIcon) update.getPhoto().getIcon());
                }
                update(p);
                update.getUpdateReset().doClick();
            } else {
                JOptionPane.showMessageDialog(update, "Wrong arguments: data marked with * are required.", "Update - Peoplev1.0", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource()
                == menu.getReadAll()) {
            ArrayList<Person> s = readAll();
            if (s.isEmpty()) {
                JOptionPane.showMessageDialog(readAll, "There are not people in BBDD", "Read All", JOptionPane.WARNING_MESSAGE);
            } else {
                readAll = new ReadAll(menu, true);
                DefaultTableModel model = (DefaultTableModel) readAll.getTable().getModel();
                for (int i = 0; i < s.size(); i++) {
                    model.addRow(new Object[i]);
                    model.setValueAt(s.get(i).getNif(), i, 0);
                    model.setValueAt(s.get(i).getName(), i, 1);
                    if (s.get(i).getDateOfBirth() != null) {
                        model.setValueAt(s.get(i).getDateOfBirth().toString(), i, 2);
                    } else {
                        model.setValueAt("", i, 2);
                    }
                    if (s.get(i).getPhoto() != null) {
                        model.setValueAt("yes", i, 3);
//                        s.get(i).getPhoto().
                    } else {
                        model.setValueAt("no", i, 3);
                    }
                }
                readAll.setVisible(true);
            }

        }

//        } else if (e.getSource() == menuStu.getDeleteAll()) {
//            int optionSelected = JOptionPane.showConfirmDialog(menuStu,
//                    "Are you sure to delete ALL BBDD?", "Delete All Students", JOptionPane.YES_NO_CANCEL_OPTION);
//            if (optionSelected == 0) {
//                deleteAllStudents();
//            } else if (optionSelected == 1) {
//            }
//        }
//    }
//
    }

    @Override
    public void insert(Person p) {
//        try {
        if (dao.read(p) == null) {
            int registeredStudents = dao.insert(p);
            if (registeredStudents == 1) {
                JOptionPane.showMessageDialog(insert, "Person with NIF " + p.getNif() + " has been registered.", "Insert - People v1.0", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(insert, "There's a person with NIF  " + p.getNif() + " registered.", "Insert - People v1.0", JOptionPane.ERROR_MESSAGE);
        }
//        } catch (DAO_Excep ex) {
//            JOptionPane.showMessageDialog(insertStu, ex.getMessage(), "BBDD Problem", JOptionPane.ERROR_MESSAGE);
//        }
    }

    @Override
    public Person read(Person p) {
        Person pTR = null;
//        try {
        if ((pTR = dao.read(p)) != null) {
        } else {
            JOptionPane.showMessageDialog(read, "There is NOT a person with NIF  " + p.getNif() + " registered.", "Read - People v1.0", JOptionPane.WARNING_MESSAGE);
        }
//        } catch (DAO_Excep ex) {
//            JOptionPane.showMessageDialog(a, ex.getMessage(), "BBDD Problem", JOptionPane.ERROR_MESSAGE);
//        }
        return pTR;
    }

    @Override
    public ArrayList<Person> readAll() {
        ArrayList<Person> people = new ArrayList<>();
        try {
            people = dao.readAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(readAll, ex.getMessage(), "BBDD Problem", JOptionPane.ERROR_MESSAGE);
        }
        return people;
    }

//    @Override
//    public void deleteAllStudents() {
//        try {
//            if (daoSt.deleteALL() >= 1) {
//                JOptionPane.showMessageDialog(deleteAllStu, "All students from BBDD have been deleted", "Delete All", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(deleteAllStu, "There were no registered students", "Delete All", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (DAO_Excep ex) {
//            JOptionPane.showMessageDialog(deleteAllStu, ex.getMessage(), "BBDD Problem", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
    @Override
    public void delete(Person personToDelete
    ) {
        if (dao.read(personToDelete) == null) {
            JOptionPane.showMessageDialog(delete, "There is NOT a person with NIF " + personToDelete.getNif() + " registered.", "Delete - People v1.0", JOptionPane.WARNING_MESSAGE);
        } else {
            if (dao.delete(personToDelete) == 1) {
                JOptionPane.showMessageDialog(delete, personToDelete.getNif() + " has been deleted.", "Delete - People v1.0", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(delete, personToDelete.getNif() + " has NOT been deleted.", "Delete - People v1.0", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void update(Person personToUpdate) {
        if (dao.read(personToUpdate) == null) {
            JOptionPane.showMessageDialog(update, "There is NOT a person with NIF " + personToUpdate.getNif() + " registered.", "Update - People v1.0", JOptionPane.WARNING_MESSAGE);
        } else {
            dao.update(personToUpdate);
            JOptionPane.showMessageDialog(update, personToUpdate.getNif() + " has been updated.", "Update - People v1.0", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
