package com.atharvredij.simplefileexplorer.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.atharvredij.simplefileexplorer.Model.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    // This method gets list of Files (Java File Objects) from specified directory

    public static List<FileModel> breadcrumbList = new ArrayList<>();

    public static List<File> getFilesFromPath(String path) {
        File file = new File(path);
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // To filter out hidden folders and file
                if(name.lastIndexOf('.') == 0) {
                    return false;
                }
                return true;
            }
        };

        return Arrays.asList(file.listFiles(filenameFilter));
    }

    // This method takes List of File (Java) objects and converts them into FileModel objects for this app

    public static List<FileModel> getFileModelsFromFiles(List<File> files) {
        List<FileModel> fileModels = new ArrayList<>();

        for (File file: files) {

            int size;
            if(file.listFiles() == null) {
                size = 0;
            }   else {
                size = file.listFiles().length;
            }

            fileModels.add(new FileModel(
                    file.getAbsolutePath(),
                    FileType.getFileType(file),
                    file.getName(),
                    convertFileSizeToMB(file.length()),
                    getExtension(file.getName()),
                    size
            ));
        }

        return fileModels;
    }

    // To convert size from long to double with 3 precision

    public static Double convertFileSizeToMB(Long sizeInBytes) {
        Double size = (sizeInBytes.doubleValue()) / (1024 * 1024);
        return BigDecimal.valueOf(size)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static String getExtension(String name) {
        return name.substring(name.lastIndexOf(".")+1);
    }

    public static String getCurrentPath() {
        String currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        List<FileModel> list = breadcrumbList.subList(1, breadcrumbList.size());
        for (FileModel f: list ) {
            currentPath = currentPath + "/" + f.name;
        }
        return currentPath;
    }

    public static void createNewFile(String name) {

        File file = new File(getCurrentPath() + "/" + name);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewFolder(String name) {

        File file = new File(getCurrentPath() + "/" + name);
        try {
            file.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);

        if(file.isDirectory()) {
            String[] children = file.list();
            for (int i=0; i<children.length; i++) {
                File child = new File(file, children[i]);
                if(child.isDirectory()) {
                    deleteFile(child.getAbsolutePath());
                } else {
                    child.delete();
                }
            }
        }

        file.delete();
    }

    public static void shareFile(Context context, FileModel fileModel) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // TODO: ISSUE - Can't share a folder Part 2
        // Folder
        if(fileModel.fileType == 1) {
            List<FileModel> listOfFile = getFileModelsFromFiles(getFilesFromPath(fileModel.path));

            ArrayList<Uri> filesUri = new ArrayList<Uri>();
            for (FileModel fm: listOfFile) {
                filesUri.add(FileProvider.getUriForFile(context,
                        context.getPackageName(),
                        new File(fm.path)));
            }

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesUri);
            context.startActivity(shareIntent);

        } else {
            Uri myUri = FileProvider.getUriForFile(context,
                    context.getPackageName(),
                    new File(fileModel.path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
            context.startActivity(shareIntent);
        }
    }

    public static void copyFile(File src, File dst) {

        new File(dst.getAbsolutePath()).mkdirs();
        String[] children = src.list();

        for (int i=0; i<children.length; i++) {
            File sourceFile1 = new File(src, children[i]);
            File destinationFile1 = new File(dst, children[i]);

            if(sourceFile1.isDirectory()) {
                new File(dst.getAbsolutePath()).mkdirs();
                copyFile(sourceFile1, destinationFile1);
            } else {
                copySingleFile(sourceFile1, destinationFile1);
            }

        }
    }

    public static void moveFile(File src, File dst) {
        copyFile(src, dst);

        deleteFile(src.getAbsolutePath());
    }

    private static void copySingleFile(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
