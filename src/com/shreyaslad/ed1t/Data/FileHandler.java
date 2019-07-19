package com.shreyaslad.ed1t.Data;

import com.shreyaslad.ed1t.Commands.OS;

import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private static String dirPath;

    public static String getDirPath() {
        return dirPath;
    }

    public static void setDirPath(String file) {
        dirPath = file;
    }

    public static void serialize() { // not going to be used for now since i'm debugging
        //write last opened dir path to file
        String os = OS.getOS();
        switch (os) {
            case "win":
                //write to C:/.edit/repo
                try {
                    FileWriter fw = new FileWriter("C:/.edit/repo");
                    fw.write(dirPath);
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } //file has been written to

                // TODO: send file path to window opener function to read from dir
                // TODO: at start of application, check if file exists and if it does, open the contents in the file
                break;
            case "mac":
                try {
                    FileWriter fw = new FileWriter("/Users/" + OS.getUsername() + "/Libary/Application Support/.edit/repo");
                    fw.write(dirPath);
                    fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                //ignore
        }
    }
}
