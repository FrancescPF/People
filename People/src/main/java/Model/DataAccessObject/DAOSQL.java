package Model.DataAccessObject;

import Model.Class.Person;
import Model.Class.PersonException;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class implements the IDAO interface and completes the function code
 * blocks so that they can operate with a SQL DDBB. The NIF is used as the
 * primary key.
 *
 * @author Francesc Perez
 * @version 1.0.0
 */
public class DAOSQL implements IDAO {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private final String JDBC_COMMU_OPT = "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "";

    private final String JDBC_DDBB = "people";
    private final String JDBC_TABLE = "person";
    private final String JDBC_DDBB_TABLE = JDBC_DDBB + "." + JDBC_TABLE;

    private final String SQL_SELECT_ALL = "SELECT * FROM " + JDBC_DDBB_TABLE + ";";
    private final String SQL_SELECT = "SELECT * FROM " + JDBC_DDBB_TABLE + " WHERE (nif = ?);";
    private final String SQL_INSERT = "INSERT INTO " + JDBC_DDBB_TABLE + " (nif, name, dateOfBirth, photo) VALUES (?, ?, ?, ?);";
    private final String SQL_UPDATE = "UPDATE " + JDBC_DDBB_TABLE + " SET name = ? dateOfBirth = ? SET photo = ? WHERE (nif = ?);";
    private final String SQL_DELETE = "DELETE FROM " + JDBC_DDBB_TABLE + " WHERE (nif = ";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_COMMU_OPT, JDBC_USER, JDBC_PASSWORD);
            createDB(conn);
            createTable(conn);
        } catch (SQLException ex) {
            //Decide what to do
        }
        return conn;
    }

    private void createDB(Connection conn) {
        String instruction = "create database if not exists " + JDBC_DDBB + ";";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(instruction);
        } catch (SQLException ex) {
            //Decide what to do
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    //Decide what to do
                }
            }
        }
    }

    private void createTable(Connection conn) {
        String query = "create table if not exists " + JDBC_DDBB + "." + JDBC_TABLE + "("
                + "nif varchar(9) primary key not null, "
                + "name varchar(50), "
                + "dateOfBirth DATE, "
                + "photo varchar(200) );";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            //Decide what to do
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    //Decide what to do
                }
            }
        }
    }

    public void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                //Decide what to do
            }
        }
    }
    
    @Override
    public Person read(Person p) throws SQLException {
        Person pReturn = null;
        Connection conn;
        PreparedStatement instruction;
        ResultSet rs;
        conn = connect();
        instruction = conn.prepareStatement(SQL_SELECT);
        instruction.setString(1, p.getNif());
        rs = instruction.executeQuery();
        while (rs.next()) {
            String nif = rs.getString("nif");
            String name = rs.getString("name");
            Date date = rs.getDate("dateOfBirth");
            String photo = rs.getString("photo");
            if (photo != null) {
                pReturn = new Person(nif, name, date, new ImageIcon(photo));
            } else {
                pReturn = new Person(nif, name, date, null);
            }
        }
        rs.close();
        instruction.close();
        disconnect(conn);
        return pReturn;
    }

    @Override
    public void insert(Person p) throws IOException, SQLException {
        Connection conn;
        PreparedStatement instruction;
        conn = connect();
        instruction = conn.prepareStatement(SQL_INSERT);
        instruction.setString(1, p.getNif());
        instruction.setString(2, p.getName());
        instruction.setDate(3, new java.sql.Date((p.getDateOfBirth()).getTime()));
        if (p.getPhoto() != null) {
            String sep = File.separator;
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + sep + "PeopleDDBB";
            String folderPhotoPath = folderPath + sep + "PhotosDDBB";
            File imagePerson = new File(folderPhotoPath + sep + p.getNif() + ".gif");
            FileOutputStream out;
            BufferedOutputStream outB;
            out = new FileOutputStream(imagePerson);
            outB = new BufferedOutputStream(out);
            BufferedImage bi = new BufferedImage(p.getPhoto().getImage().getWidth(null),
                    p.getPhoto().getImage().getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(p.getPhoto().getImage(), 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] img = baos.toByteArray();
            for (int i = 0; i < img.length; i++) {
                outB.write(img[i]);
            }
            outB.close();
            instruction.setString(4, imagePerson.getPath());
        } else {
            instruction.setString(4, null);
        }
        instruction.executeUpdate();
        instruction.close();
        disconnect(conn);
    }
    
    

    @Override
    public ArrayList<Person> readAll() throws SQLException, PersonException {
        ArrayList<Person> people = new ArrayList<>();
        Connection conn;
        Statement instruction;
        ResultSet rs;
        conn = connect();
        instruction = conn.createStatement();
        rs = instruction.executeQuery(SQL_SELECT_ALL);
        while (rs.next()) {
            String nif = rs.getString("nif");
            String name = rs.getString("name");
            Date date = rs.getDate("dateOfBirth");
            String photo = rs.getString("photo");
            if (photo != null) {
                people.add(new Person(nif, name, date, new ImageIcon(photo)));
            } else {
                people.add(new Person(nif, name, date, null));
            }
        }
        rs.close();
        instruction.close();
        disconnect(conn);
        if (people.isEmpty()) {
            throw new PersonException("There aren't people registered.");
        }
        return people;
    }

    @Override
    public void update(Person person) throws FileNotFoundException, SQLException, IOException {
        Connection conn;
        PreparedStatement instruction;
        conn = connect();
        instruction = conn.prepareStatement(SQL_UPDATE);
        instruction.setString(1, person.getName());
        instruction.setDate(2, new java.sql.Date((person.getDateOfBirth()).getTime()));
        if (person.getPhoto() != null) {
            String sep = File.separator;
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + sep + "PeopleDDBB";
            String folderPhotoPath = folderPath + sep + "PhotosDDBB";
            File imagePerson = new File(folderPhotoPath + sep + person.getNif() + ".gif");
            FileOutputStream out;
            BufferedOutputStream outB;
            out = new FileOutputStream(imagePerson);
            outB = new BufferedOutputStream(out);
            BufferedImage bi = new BufferedImage(person.getPhoto().getImage().getWidth(null),
                    person.getPhoto().getImage().getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(person.getPhoto().getImage(), 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] img = baos.toByteArray();
            for (int i = 0; i < img.length; i++) {
                outB.write(img[i]);
            }
            outB.close();
            instruction.setString(3, imagePerson.getPath());
        } else {
            instruction.setString(3, null);
        }
        instruction.executeUpdate();
        instruction.close();
        disconnect(conn);
    }

    @Override
    public int delete(Person person) {
        Connection conn = null;
        PreparedStatement instruction = null;
        int registers = 0;
        try {
            conn = connect();
            String query = SQL_DELETE + "'" + person.getNif() + "'" + ");";
            instruction = conn.prepareStatement(query);
            registers = instruction.executeUpdate();
        } catch (SQLException ex) {
            //Decide what to do
        } finally {
            try {
                if (instruction != null) {
                    instruction.close();
                }
                disconnect(conn);
            } catch (SQLException ex) {
                //Decide what to do
            }
        }
        return registers;
    }

}
