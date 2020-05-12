package com.atharvredij.simplefileexplorer.Utils;

import java.io.File;

public class FileType {

    // This class only defines constant for File and Folder

    public static final int FILE = 0;
    public static final int FOLDER = 1;

    // This method determines if file object is Folder or File

    public static int getFileType(File file) {
        if(file.isDirectory()) {
            return FOLDER;
        } else {
            return FILE;
        }
    }
}
