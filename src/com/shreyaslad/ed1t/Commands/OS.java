package com.shreyaslad.ed1t.Commands;

import com.shreyaslad.ed1t.Data.FileHandler;
import com.shreyaslad.ed1t.Ed1t;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;

public class OS {

    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String username = System.getProperty("user.name");

    private static boolean isFirstLaunch;

    private static File winLock = new File("C:/.ed1t/repo");
    private static File macLock = new File("/Users/" + username + "/Library/Application Support/.edit/repo");

    @SuppressWarnings("Duplicates")
    public static void init() { //not calling this yet for debugging purposes
        switch (OS) {
            case "win":
                if (winLockExists()) {
                    isFirstLaunch = false;
                    StringBuilder str = new StringBuilder();
                    String line;

                    try {
                        FileReader fileReader = new FileReader(winLock);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        while ((line = bufferedReader.readLine()) != null) {
                            str.append(line).append("\n");
                        }

                        bufferedReader.close();

                        String result = str.toString();
                        new Ed1t(result);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (GitAPIException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    isFirstLaunch = true;
                    Ed1t.toggleMain(false);
                    Events.createFirstStartWindow();
                    try {
                        FileWriter fw = new FileWriter(winLock); //create lock file to signify that this is the first and only first start
                        fw.write("");
                        fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "mac":
                if (macLockExists()) {
                    isFirstLaunch = false;
                    try {
                        StringBuilder str = new StringBuilder();
                        FileReader fileReader = new FileReader(macLock);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            str.append(line).append("\n");
                        }

                        bufferedReader.close();

                        String result = str.toString();
                        new Ed1t(result);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (GitAPIException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    isFirstLaunch = true;
                    Ed1t.toggleMain(false);
                    Events.createFirstStartWindow(); //this is the first start
                    try {
                        FileWriter fw = new FileWriter(macLock); //create lock file
                        fw.write("");
                        fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            default:
                System.out.println("This operating system is not supported at the moment. Please use Windows or MacOS.");
                System.exit(0);
        }
    }

    public static boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public static boolean winLockExists() {
        return winLock.exists();
    }

    public static boolean macLockExists() {
        return macLock.exists();
    }

    public static String getOS() {
        return OS;
    }

    public static String getUsername() {
        return username;
    }

    public static String getWinLockCanonical() {
        return winLock.toString();
    }

    public static String getMacLockCanonical() {
        return macLock.toString();
    }


    // yes I'm aware that I don't need the switch case
    @SuppressWarnings("Duplicates")
    public static void setLastLogin(String file) {
        switch (OS) {
            case "win":
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(FileHandler.getDirPath());
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "mac":
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(FileHandler.getDirPath());
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                //ignore
        }
    }
}
