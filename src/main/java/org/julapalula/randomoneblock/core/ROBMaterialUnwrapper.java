package org.julapalula.randomoneblock.core;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.julapalula.randomoneblock.utils.ROBFileManagerUtil;

import java.io.FileWriter;
import java.util.Arrays;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.List;
import java.util.stream.Collectors;

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
 *   <li>Creates a example .lot.json if don't have any.</li>
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

public class ROBMaterialUnwrapper implements Listener {
    ROBFileManagerUtil fileManager = new ROBFileManagerUtil();
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
                System.out.println("[ROB] lots folder created with success in "+ folder.getAbsolutePath() +".");
            } else {
                System.out.println("[ROB] Failed to create lots directory.");
                return new ArrayList<>();
            }
        }

        // List .lot.json files in the directory
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".lot.json") && !fileManager.hasSpecialCharacters(fileManager.getFileNameWithoutExtension(name)));

        if (files != null && files.length > 0) {
            System.out.println("[ROB] Founded " + files.length + " lots.");
            return Arrays.asList(files); // Convert the File[] array to a List<File>
        } else {
            System.out.println("[ROB] No .lot.json files found in the directory: " + folder.getAbsolutePath());
            System.out.println("[ROB] Creating all.lot.json " + folder.getAbsolutePath());
            //Creates a all lot (dummy example)
            createDummyLotJson(directoryPath);
            //Retry to get the files
            files = folder.listFiles((dir, name) -> name.endsWith(".lot.json") && !fileManager.hasSpecialCharacters(fileManager.getFileNameWithoutExtension(name)));

            return Arrays.asList(files); // Return an empty list if no files are found
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
                System.err.println("[ROB Lod Error] Invalid negative score from " + fileManager.getFileNameWithoutExtension(file.getName()) + ".This lot will be omitted.");
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
                    System.err.println("[ROB Lod Error] Invalid material name: " + materialName + " from " + fileManager.getFileNameWithoutExtension(file.getName()));
                }
            }

            if(!isLodValid) { //In case is not valid, we omit this lot
                System.out.println("[ROB Lod Error] Check " + fileManager.getFileNameWithoutExtension(file.getName()) + " lot to resolve invalid material names. This lot will be omitted.");
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
        ArrayList<Lot> lots = new ArrayList<>();

        for (File file : lotFiles) {
            Lot lot = extractLotData(file);
            if (lot != null) {
                lots.add(lot);
            }
        }
        return lots;
    }

    /**
    * Creates a dummy.lot.json for Minecraft version 1.21.1.
    */

    private void createDummyLotJson(String directoryPath) {
        File dummyFile = new File(directoryPath, "all.lot.json");

        if (!dummyFile.exists()) {
            try {
                JSONObject dummyJson = new JSONObject();
                dummyJson.put("score", 0); // Add score to JSON object

                // Get all material names
                List<String> materialNames = Arrays.stream(Material.values())
                        .map(Material::name)
                        .collect(Collectors.toList());

                // Add materials to JSON object
                dummyJson.put("materials", materialNames);

                // Write JSON to file
                try (FileWriter writer = new FileWriter(dummyFile)) {
                    writer.write(dummyJson.toString());
                    System.out.println("[ROB] Created dummy lot file: " + dummyFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("[ROB Error] Failed to create dummy file: " + dummyFile.getName());
                e.printStackTrace();
            }
        } else {
            System.out.println("[ROB] Dummy file already exists: " + dummyFile.getAbsolutePath());
        }
    }

}

