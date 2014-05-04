
package com.gizrak.ebook.utils;

import java.util.ArrayList;

public class FileUtils {

    public static final ArrayList<String> SUPPORT_FILE_LIST = new ArrayList<String>();
    static {
        SUPPORT_FILE_LIST.add("epub");
        SUPPORT_FILE_LIST.add("txt");
        SUPPORT_FILE_LIST.add("html");
    }

    private FileUtils() {
    }
}
