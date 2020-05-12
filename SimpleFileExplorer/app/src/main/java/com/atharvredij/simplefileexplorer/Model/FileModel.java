package com.atharvredij.simplefileexplorer.Model;

public class FileModel {

    // This is the class which defines a File object for this app

    public String path;
    public int fileType;
    public String name;
    public Double sizeInMB;
    public String extension = "";
    public int subFiles = 0;

    public FileModel(String path, int fileType, String name, Double sizeInMB, String extension, int subFiles) {
        this.path = path;
        this.fileType = fileType;
        this.name = name;
        this.sizeInMB = sizeInMB;
        this.extension = extension;
        this.subFiles = subFiles;
    }
}
