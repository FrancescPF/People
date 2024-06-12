package Start;

import java.io.File;

/**
 *
 * @author Fran Perez
 * @version 1.1.0
 */
public enum Routes {

    APPIMAGES("images", null, null),
    FILE ("File", "Photos", "dataFile.txt" ),
    FILES ("FileSer", "Photos", "dataFile.ser" );
//    DB,
//    DBS;

    private final String folderPath;
    private final String folderPhotos;
    private final String dataFile;
    
    private Routes (String folderPath, String folderPhotos, String dataFile){
        String userDir = System.getProperty("user.dir");
        this.folderPath = userDir + File.separator + folderPath;
        this.folderPhotos = this.folderPath + File.separator + folderPhotos;
        this.dataFile = this.folderPath + File.separator + dataFile;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getFolderPhotos() {
        return folderPhotos;
    }

    public String getDataFile() {
        return dataFile;
    }
    
    
    
}
