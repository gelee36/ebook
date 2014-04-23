
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

    /**
     * Returns the <a
     * href="http://en.wikipedia.org/wiki/Filename_extension">file extension</a>
     * for the given file name, or the empty string if the file has no
     * extension. The result does not include the '{@code .}'.
     */
    public static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
