package com.shreyaslad.ed1t.Data;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.HashMap;

public class RemoteList {

    private static HashMap<String, String> remoteMap = new HashMap<>();
    private static String selectedRemote;

    public static void addRemotes(String remote, String url) {
        UrlValidator validator = new UrlValidator();
        if (validator.isValid(url)) {
            //ignore
        }

        remoteMap.put(remote, url);
    }

    public static void removeRemote(String remote) {
        remoteMap.remove(remote);
    }

    public static int size() {
        return remoteMap.size();
    }

    public static void editURL(String remote, String newURL) {
        UrlValidator validator = new UrlValidator();
        if (validator.isValid(newURL)) {
            // ignore
        }

        remoteMap.put(remote, newURL);
    }

    public static String get(String remote) {
        return remoteMap.get(remote);
    }

    public static String get(int index) {
        return remoteMap.get(index);
    }

    public static void setSelectedRemote(String remote) {
        if (!remoteMap.containsKey(remote)) {
            //ignore
        }

        selectedRemote = remote;
    }

    public static String getSelectedRemote() {
        return selectedRemote;
    }

}
