/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataAccessObject;

import Model.Class.Person;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Fran Perez
 */
public class DAOSQL implements IDAO {

    //Variables para la conexión segura contra el servidor (sin especificar DDBB)
    private final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private final String JDBC_COMMU_OPT = "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "";

    //Especificamos la base de Datos
    private final String JDBC_DDBB = "people";
    private final String JDBC_TABLE = "person";
    private final String JDBC_DDBB_TABLE = JDBC_DDBB + "." + JDBC_TABLE;

    //Variables para las consultas SQL
//    private final String SQL_SELECT_ALL = "SELECT * FROM " + JDBC_DDBB_TABLE + ";";
    private final String SQL_SELECT = "SELECT * FROM " + JDBC_DDBB_TABLE + " WHERE (nif = ?);";
//    private final String SQL_SELECT2 = "SELECT * FROM " + JDBC_DDBB_TABLE + " WHERE (age = ";
    private final String SQL_INSERT = "INSERT INTO " + JDBC_DDBB_TABLE + " (nif, name, dateOfBirth, photo) VALUES (?, ?, ?, ?);";
    private final String SQL_UPDATE = "UPDATE " + JDBC_DDBB_TABLE + " SET name = ? dateOfBirth = ? SET photo = ? WHERE (nif = ?);";
    private final String SQL_DELETE = "DELETE FROM " + JDBC_DDBB_TABLE + " WHERE (nif = ";
//    private final String SQL_DELETE_ALL = "DELETE FROM " + JDBC_DDBB_TABLE + ";";
//    private final String SQL_RESET_AGES = "UPDATE " + JDBC_DDBB_TABLE + " SET age = 0 WHERE (name = ?);";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_COMMU_OPT, JDBC_USER, JDBC_PASSWORD);
            createDB(conn);
            createTable(conn);
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            System.out.println("Can not connect to mysql Server: ");
        }
        return conn;
    }

    private void createDB(Connection conn) {
        //Sentencia SQL que crea la BBDD si no existe en el servidor
        String instruction = "create database if not exists " + JDBC_DDBB + ";";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(instruction);
        } catch (SQLException ex) {
            System.out.println("No se ha podido crear la base de datos: " + JDBC_DDBB);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println("No se ha podido cerrar el stmt");
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
            System.out.println("NO se ha podido crear la tabla " + JDBC_TABLE);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println("No se ha podido cerrar el stmt");
                }
            }
        }
    }

    public void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Can not disconnect from database " + JDBC_DDBB);
            }
        }
    }
//
//    @Override
//    public List<Student> readALL() throws DAO_Excep {
//        List<Student> students = new ArrayList<>();
//        Connection conn = null;
//        Statement instruction = null;
//        ResultSet rs = null;
//        try {
//            conn = connect();
//            instruction = conn.createStatement();
//            rs = instruction.executeQuery(SQL_SELECT_ALL);
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String nombre = rs.getString("name");
//                int edad = rs.getInt("age");
//                students.add(new Student(id, nombre, edad));
//            }
//        } catch (SQLException ex) {
//            //ex.printStackTrace(System.out);
//            throw new Read_SQL_DAO_Excep("Can not read from database - readAll");
//        } finally {
//            try {
//                rs.close();
//                instruction.close();
//                disconnect(conn);
//            } catch (SQLException ex) {
//                //ex.printStackTrace(System.out);
//                throw new Read_SQL_DAO_Excep("Can not read from database - readAll");
//            }
//        }
//        return students;
//    }
//

    @Override
    public Person read(Person p) {
        Person pReturn = null;
        Connection conn = null;
        PreparedStatement instruction = null;
        ResultSet rs = null;
        try {
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
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            System.out.println("Can not read from database (DAO_COntroller.DAOSQL.read)");
        } finally {
            try {
                rs.close();
                instruction.close();
                disconnect(conn);
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                System.out.println("Can not read from database (DAO_COntroller.DAOSQL.read)");
            }
        }
        return pReturn;
    }

//    @Override
//    public List<Student> readByAge(Student s) throws DAO_Excep {
//        ArrayList<Student> students = new ArrayList<>();
//        Student student = null;
//        Connection conn = null;
//        Statement instruction = null;
//        ResultSet rs = null;
//        try {
//            conn = connect();
//            String query = SQL_SELECT2 + "'" + s.getAge() + "'" + ");";
//            System.out.println(query);
//            instruction = conn.createStatement();
//            rs = instruction.executeQuery(query);
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String nam = rs.getString("name");
//                int age = rs.getInt("age");
//                student = new Student(id, nam, age);
//                students.add(student);
//            }
//        } catch (SQLException ex) {
//            //ex.printStackTrace(System.out);
//            throw new Read_SQL_DAO_Excep("Can not read from database (DAO_COntroller.DAOSQL.read)");
//        } finally {
//            try {
//                rs.close();
//                instruction.close();
//                disconnect(conn);
//            } catch (SQLException ex) {
//                //ex.printStackTrace(System.out);
//                throw new Read_SQL_DAO_Excep("Can not close database read process (DAO_COntroller.DAOSQL.read)");
//            }
//        }
//        return students;
//    }
//
    @Override
    public int insert(Person p) {
        Connection conn = null;
        PreparedStatement instruction = null;
        int registers = 0;
        try {
            conn = connect();
            instruction = conn.prepareStatement(SQL_INSERT);
            instruction.setString(1, p.getNif());
            instruction.setString(2, p.getName());
            instruction.setDate(3, new java.sql.Date((p.getDateOfBirth()).getTime()));
            if (p.getPhoto() != null) {
                String sep = File.separator;
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + sep + "People";
                File folderProject = new File(folderPath);
                String folderPhotoPath = folderPath + sep + "PhotosBBDD";
                File imagePerson = new File(folderPhotoPath + sep + p.getNif() + ".gif");
                FileOutputStream out = null;
                BufferedOutputStream outB = null;
                try {
                    try {
                        out = new FileOutputStream(imagePerson);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                } catch (IOException ex) {
                    Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (outB != null) {
                        try {
                            outB.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                instruction.setString(4, imagePerson.getPath());
            } else {
                instruction.setString(4, null);
            }
            System.out.println("4");
            registers = instruction.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                instruction.close();
                disconnect(conn);
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                System.out.println("Can not close database write process (DAO_COntroller.DAOSQL.insert)");
            }
        }
        //Devolvemos la cantidad de registros afectados, en nuestro caso siempre uno
        return registers;
    }

    @Override
    public int update(Person person) {
        Connection conn = null;
        PreparedStatement instruction = null;
        int registers = 0;
        try {
            conn = connect();
            instruction = conn.prepareStatement(SQL_UPDATE);
            instruction.setString(1, person.getName());
            instruction.setDate(2, new java.sql.Date((person.getDateOfBirth()).getTime()));
            if (person.getPhoto() != null) {
                String sep = File.separator;
                String projectPath = System.getProperty("user.dir");
                String folderPath = projectPath + sep + "People";
                File folderProject = new File(folderPath);
                String folderPhotoPath = folderPath + sep + "PhotosBBDD";
                File imagePerson = new File(folderPhotoPath + sep + person.getNif() + ".gif");
                FileOutputStream out = null;
                BufferedOutputStream outB = null;
                try {
                    try {
                        out = new FileOutputStream(imagePerson);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    outB = new BufferedOutputStream(out);
                    BufferedImage bi = new BufferedImage(person.getPhoto().getImage().getWidth(null),
                            person.getPhoto().getImage().getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);
                    bi.getGraphics().drawImage(person.getPhoto().getImage(), 0, 0, null);
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
                } catch (IOException ex) {
                    Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (outB != null) {
                        try {
                            outB.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DAOSQL.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                instruction.setString(3, imagePerson.getPath());
            } else {
                instruction.setString(3, null);
            }
            //cada vez que modificamos una base de datos llamamos a executeUpdate()
            registers = instruction.executeUpdate();
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            System.out.println("Can not write to database (DAO_COntroller.DAOSQL.update)");
        } finally {
            try {
                instruction.close();
                disconnect(conn);
            } catch (SQLException ex) {
                //ex.printStackTrace(System.out);
                System.out.println("Can not close database write process (DAO_COntroller.DAOSQL.update)");
            }
        }
        //Devolvemos la cantidad de registros afectados
        return 1;
    }

    @Override
    public int delete(Person person) {
        Connection conn = null;
        PreparedStatement instruccion = null;
        int registers = 0;
        try {
            conn = connect();
            String query = SQL_DELETE + "'" + person.getNif() + "'" + ");";
            instruccion = conn.prepareStatement(query);
            //cada vez que modificamos una base de datos llamamos a executeUpdate()
            registers = instruccion.executeUpdate();
        } catch (SQLException ex) {
            //ex.printStackTrace(System.out);
            System.out.println("Can not write to database (DAO_Controller.DAOSQL.delete)");

        } finally {
            try {
                instruccion.close();
                disconnect(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
                System.out.println("Can not close database write process (DAO_COntroller.DAOSQL.delete)");
            }
        }
        //Devolvemos la cantidad de registros afectados
        return 1;
    }
//
//    @Override
//    public int deleteALL() throws DAO_Excep {
//        Connection conn = null;
//        PreparedStatement instruccion = null;
//        int registers = 0;
//        try {
//            conn = connect();
//            instruccion = conn.prepareStatement(SQL_DELETE_ALL);
//            //cada vez que modificamos una base de datos llamamos a executeUpdate()
//            registers = instruccion.executeUpdate();
//        } catch (SQLException ex) {
//            //ex.printStackTrace(System.out);
//            throw new Write_SQL_DAO_Excep("Can not write to database (DAO_Controller.DAOSQL.deleteAll)");
//        } finally {
//            try {
//                instruccion.close();
//                disconnect(conn);
//            } catch (SQLException ex) {
//                ex.printStackTrace(System.out);
//                throw new Write_SQL_DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.deleteAll)");
//            }
//        }
//        //Devolvemos la cantidad de registros afectados
//        return registers;
//    }
//
//    @Override
//    public int resetAges() throws DAO_Excep {
//        //Esta operación se podría hacer con una única consulta SQL
//        //pero no lo hacemos así porque es un ejemplo de transacción
//        Connection conn = null;
//        PreparedStatement instruction = null;
//        int registers = 0;
//        try {
//            List<Student> students = readALL();
//            conn = connect();
//            conn.setAutoCommit(false);
//            if (!students.isEmpty()) {
//                for (Student a : students) {
//                    instruction = conn.prepareStatement(SQL_RESET_AGES);
//                    instruction.setString(1, a.getName());
//                    //cada vez que modificamos una base de datos llamamos a executeUpdate()
//                    registers += instruction.executeUpdate(); 
//                    //Activar para comprobar el funcionamiento del rollback
//                    //Debe haber más de un estudiante en la Base de datos (*)
////                    throw new SQLException();
//                }
//            }
//        } catch (SQLException ex) {
//            if(conn != null){
//                try {
//                    conn.rollback();
//                } catch (SQLException ex1) {
//                    System.out.println("ROLLBACK");
//                }
//                //(*)
////                registers=0;
//            }
//        } finally {
//            try {
//                instruction.close();
//                conn.setAutoCommit(true);
//                disconnect(conn);
//            } catch (SQLException ex) {
//                ex.printStackTrace(System.out);
//                throw new Write_SQL_DAO_Excep("Can not close database write process (DAO_COntroller.DAOSQL.deleteAll)");
//            }
//        }
//        //Devolvemos la cantidad de registros afectados
//        return registers;
//    }
//
//    
}
