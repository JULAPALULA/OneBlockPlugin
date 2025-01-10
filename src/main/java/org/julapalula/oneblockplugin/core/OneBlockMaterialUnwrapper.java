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

/**
 * A utility class for handling `.lot.json` files.
 *
 * <p>
 * This class implements the `Listener` interface and provides methods to:
 * <ul>
 *   <li>Retrieve a list of `.lot.json` files from lot directory (located in main server directory).</li>
 *   <li>Parse the content of `.lot.json` files into `Lot` objects.</li>
 *   <li>Validate if materials are Minecraft registered stuff and validate score attributes of the lots.</li>
 *   <li>Retrieve valid `Lot` objects from a .lot.json` files.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The class ensures that:
 * <ul>
 *   <li>Directories are created if they do not exist.</li>
 *   <li>Files with invalid material names or scores are omitted with appropriate logging.</li>
 *   <li>Special characters in file names are avoided.</li>
 * </ul>
 * </p>
 *
 * <strong>Usage:</strong>
 * <pre>
 * OneBlockMaterialUnwrapper unwrapper = new OneBlockMaterialUnwrapper();
 * ArrayList<Lot> lots = unwrapper.loadLods("/path/to/directory");
 * </pre>
 *
 * <strong>Dependencies:</strong>
 * <ul>
 *   <li>{@code OneBlockFileManagerUtil}: Utility for file management operations.</li>
 *   <li>{@code JSONParser}: Used for parsing JSON files.</li>
 *   <li>{@code Lot}: Represents a lot with attributes like materials and score.</li>
 *   <li>{@code Material}: Represents a material entity in the system.</li>
 * </ul>
 *
 */

public class OneBlockMaterialUnwrapper implements Listener {
    OneBlockFileManagerUtil fileManager = new OneBlockFileManagerUtil();
    JSONParser parser = new JSONParser();

    /**
     * Get the list of .lot.json files in the specified directory.
     *
     * @param directoryPath the path to the directory
     * @return a list of File objects representing .lot.json files
     */

    private List<File> getLotFiles(String directoryPath) {
        File folder = new File(directoryPath);

        // Check if the folder exists
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("[OneBlockPlugin] lots folder created with success in "+ folder.getAbsolutePath() +".");
            } else {
                System.out.println("[OneBlockPlugin] Failed to create lots directory.");
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

    private Lot extractLotData(File file) {
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

    public ArrayList<Lot> loadLots(String directoryPath) {
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

