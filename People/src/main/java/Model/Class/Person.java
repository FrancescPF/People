/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Class;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 *
 * @author Fran Perez
 */
public class Person implements Serializable{

    //Person attributes
    private String name;
    private String nif;
    private Date dateOfBirth;
    private ImageIcon photo;

    public Person(){
        
    }
    
    
    //Constructor to validate new person.
    //Two persons cannot have the same NIF
    public Person(String nif) {
        this.nif = nif;
    }

    //Constructor with mandatory data
    public Person(String name, String nif) {
        this.name = name;
        this.nif = nif;
    }

    //Constructor with everything
    public Person(String name, String nif, Date dateOfBirth, ImageIcon photo) {
        this.name = name;      
        this.nif = nif;
        this.dateOfBirth = dateOfBirth;
        this.photo = photo;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ImageIcon getPhoto() {
        return photo;
    }

    public void setPhoto(ImageIcon photo) {
        this.photo = photo;
    }
    
    //Used to compare two persons by their nifs
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.nif);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        return Objects.equals(this.hashCode(), other.hashCode());
    }

    //Used to show person's inform by console. Only for debugging pourposes.
    @Override
    public String toString() {
        return "Student{" + "Name= " + name + ", NIF= " + nif
                + ", DateOfBirth= " + dateOfBirth + ", Photo= " + (photo!=null) +'}';
    }

}
