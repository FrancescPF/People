package View;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Francesc Perez
 * @version 1.0.0
 */
public class DropPhotoListener implements DropTargetListener {

    JDialog insert;
    JLabel label;
    String dragText = "Drag photo here!";
    String initText;

    public DropPhotoListener(JLabel label, JDialog insert) {
        this.label = label;
        this.insert = insert;
        initText = label.getText();   
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        label.setText(dragText);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        label.setText(initText);
    }

    @Override
    public void drop(DropTargetDropEvent ev) {
        ev.acceptDrop(DnDConstants.ACTION_COPY);
        //WE GET DROPPED ITEM
        Transferable t = ev.getTransferable();
        //GET DATA FORMAT OF THE ITEM
        DataFlavor df[] = t.getTransferDataFlavors();   
        //LOOP THROUGH FLAVORS
        for(DataFlavor f : df){
            try{
//                //CHECK IF ITEMS ARE FILE TYPES
                if(f.isFlavorJavaFileListType()){
//                    //GET LIST OF ITEMS
                    List<File> files = (List<File>)t.getTransferData(f);
//                    //LOOP THROUGH THEM
                    for(File file : files){
                        String extension = FilenameUtils.getExtension(file.getName());
                        if(file.length() <= 64000 && (extension.equals("gif") || extension.equals("png") ||
                                extension.equals("jfif") || extension.equals("jpg")))
                            displayImage(file.getPath());
                        else
                            throw new Exception("The image must be less than 64KB and in the specified format");
                    }
                }
            }catch ( Exception ex){
                JOptionPane.showMessageDialog(insert,ex.getMessage() , "WRONG FILE", JOptionPane.ERROR_MESSAGE);
            }
        }      
    }

    private void displayImage(String path) {
        try {
            //Supported GIF, PNG, JPEG, BMP, WBMP 
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            img = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT);
            icon = new ImageIcon(img);
            label.setIcon(icon);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(insert, "Only .GIF, .JPEG, .PNG, .BMP and .WBMP images can be readed", "WRONG FILE FORMAT", JOptionPane.ERROR_MESSAGE);
        }
    }

}
