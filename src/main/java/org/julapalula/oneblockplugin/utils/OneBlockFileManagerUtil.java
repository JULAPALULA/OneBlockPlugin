package org.julapalula.oneblockplugin.utils;

import java.io.File;

public class OneBlockFileManagerUtil {

    /**
     * Extracts the file name without the extension from a given file name.
     * If the file name ends with ".lot.json", the extension will be removed.
     *
     * @param fileFullName the full file name (with extension).
     * @return the file name without the ".lot.json" extension, or the original file name if it doesn't have that extension.
     */

    public String getFileNameWithoutExtension(String fileFullName) {
        if (fileFullName.endsWith(".lot.json")) {
            return fileFullName.substring(0, fileFullName.length() - ".lot.json".length());
        } else {
            return fileFullName;
        }
    }

    /**
     * Checks if the input string contains any special characters that are not alphanumeric or periods.
     *
     * @param input the input string to check.
     * @return true if the input contains special characters, false otherwise.
     */

    public boolean hasSpecialCharacters(String input) {
        String regex = "[^a-zA-Z0-9.]";

        return input.matches(".*" + regex + ".*");
    }

}
