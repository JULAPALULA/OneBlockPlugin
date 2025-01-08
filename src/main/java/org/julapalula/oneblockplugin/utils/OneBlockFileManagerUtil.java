package org.julapalula.oneblockplugin.utils;

import java.io.File;

public class OneBlockFileManagerUtil {
    public String getFileNameWithoutExtension(String fileFullName) {
        if (fileFullName.endsWith(".lot.json")) {
            return fileFullName.substring(0, fileFullName.length() - ".lot.json".length());
        } else {
            return fileFullName;
        }
    }
    public boolean hasSpecialCharacters(String input) {
        String regex = "[^a-zA-Z0-9.]";

        return input.matches(".*" + regex + ".*");
    }

}
