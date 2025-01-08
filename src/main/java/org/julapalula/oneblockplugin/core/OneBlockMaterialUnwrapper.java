package org.julapalula.oneblockplugin.core;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.julapalula.oneblockplugin.utils.OneBlockFileManagerUtil;
import java.util.Arrays;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.List;

public class OneBlockMaterialUnwrapper implements Listener {
    OneBlockFileManagerUtil fileManager = new OneBlockFileManagerUtil();
    JSONParser parser = new JSONParser();

    /**
     * Get the list of .lot.json files in the specified directory.
     *
     * @param directoryPath the path to the directory
     * @return a list of File objects representing .lot.json files
     */

    protected List<File> getLotFiles(String directoryPath) {
        File folder = new File(directoryPath);

        // Check if the folder exists
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("[OneBlockPlugin] lods folder created with success in "+ folder.getAbsolutePath() +".");
            } else {
                System.out.println("[OneBlockPlugin] Failed to create lods directory.");
                return new ArrayList<>();
            }
        }

        // List .lot.json files in the directory
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".lot.json") && !fileManager.hasSpecialCharacters(fileManager.getFileNameWithoutExtension(name)));

        if (files != null && files.length > 0) {
            System.out.println("[OneBlockPlugin] Founded " + files.length + " lots.");
            return Arrays.asList(files); // Convert the File[] array to a List<File>
        } else {
            System.out.println("[OneBlockPlugin] No .lot.json files found in the directory: " + folder.getAbsolutePath());
            return new ArrayList<>(); // Return an empty list if no files are found
        }
    }

    /**
     * Extract the JSON content from a given file.
     *
     * @param file the JSON file to parse
     * @return the parsed JSONObject, or null if parsing fails
     */

    protected Lot extractLotData(File file) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // Extract data and map to Lot
            Lot lot = new Lot();
            lot.setLotName(fileManager.getFileNameWithoutExtension(file.getName()));

            List<String> materialNames = (List<String>) jsonObject.get("materials");
            List<Material> materials = new ArrayList<>();

            boolean isLodValid = true;

            int materialScore = ((Long) jsonObject.get("score")).intValue();
            //Checks if the actual score is not negative
            if(materialScore < 0) {
                System.err.println("[OneBlockPlugin Lod Error] Invalid negative score from " + fileManager.getFileNameWithoutExtension(file.getName()) + ".This lot will be omitted.");
                return null;
            }
            lot.setScore(materialScore); //set score to lod

            for (String materialName : materialNames) {
                // Attempt to cast string to Material
                Material material = Material.getMaterial(materialName);
                if (material != null) {
                    materials.add(material);  // Valid material name, add to the list
                } else {
                    isLodValid = false;
                    System.err.println("[OneBlockPlugin Lod Error] Invalid material name: " + materialName + " from " + fileManager.getFileNameWithoutExtension(file.getName()));
                }
            }

            if(!isLodValid) { //In case is not valid, we omit this lot
                System.out.println("[OneBlockPlugin Lod Error] Check " + fileManager.getFileNameWithoutExtension(file.getName()) + " lot to resolve invalid material names. This lot will be omitted.");
                return null;
            }

            lot.setMaterials(materials);  // Set the materials in the Lot object
            return lot;
        } catch (IOException e) {
            System.err.println("Failed to read file: " + file.getName());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Failed to parse JSON in file: " + file.getName());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load all .lot.json files from a directory and parse their contents.
     *
     * @param directoryPath the path to the directory
     */

    protected ArrayList<Lot> loadLods(String directoryPath) {
        List<File> lotFiles = getLotFiles(directoryPath);
        ArrayList<Lot> lods = new ArrayList<>();

        for (File file : lotFiles) {
            Lot lot = extractLotData(file);
            if (lot != null) {
                lods.add(lot);
            }
        }
        return lods;
    }

}

