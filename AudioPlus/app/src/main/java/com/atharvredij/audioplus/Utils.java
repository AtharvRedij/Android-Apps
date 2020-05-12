package com.atharvredij.audioplus;

import android.media.MediaPlayer;
import android.os.Environment;

import com.atharvredij.audioplus.UI.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {



    public static String fileNameGenerator() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static List<String> getListOfNames() {
        MainActivity.createFolder();
        String path = Environment.getExternalStorageDirectory().toString()+"/AudioPlus";
        File directory = new File(path);
        File[] files = directory.listFiles();

        List<String> names = new ArrayList<>();

        if(files == null) {
            return names;
        }

        for (int i = 0; i < files.length; i++)
        {
            names.add(files[i].getName());
        }

        return names;
    }

    public static void playRecording(String name) {


    }


}
