package com.example.horus.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by lognyun on 2018/12/3 11:18:15
 */
public class Installation {


    private static String sID = null;

    private static final String INSTALLATION = "9923489983818.txt";


    public synchronized static File getIMEIFile() {
       String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                            +File.separator+INSTALLATION;
        return new File(path);
    }


    public synchronized static String id() {

        if (sID == null) {

            File installation = getIMEIFile();

            try {

                if (!installation.exists())

                    writeInstallationFile(installation);

                sID = readInstallationFile(installation);

            } catch (Exception e) {

                throw new RuntimeException(e);

            }

        }

        return sID;

    }


    public static String readInstallationFile(File installation) throws IOException {

        RandomAccessFile f = new RandomAccessFile(installation, "r");

        byte[] bytes = new byte[(int) f.length()];

        f.readFully(bytes);

        f.close();

        return new String(bytes);

    }


    public static String writeInstallationFile(File installation, String id) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        out.write(id.getBytes());
        out.close();

        return id;
    }

    public static String  writeInstallationFile(File installation) throws IOException {
        String id=UUID.randomUUID().toString();
        writeInstallationFile(installation, id);
        return id;
    }
}
