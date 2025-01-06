package org.julapalula.oneblockplugin.lots;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MaterialLots {

    private static final Map<String, Material[]> lots = new HashMap<>();
    private static final Random random = new Random();

    static {
        lots.put("basic", new Material[]{
                Material.ACACIA_FENCE,
                Material.PRISMARINE_BRICK_SLAB,
                Material.ORANGE_TERRACOTTA,
                Material.CYAN_TERRACOTTA,
                Material.POLISHED_BLACKSTONE_BRICK_STAIRS,
                Material.AZALEA,
                Material.GRAVEL,
                Material.TNT,
                Material.TNT_MINECART,
                Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,
                Material.ACACIA_LEAVES,
                Material.BIRCH_BOAT,
                Material.DARK_OAK_BOAT,
                Material.ACACIA_BUTTON,
                Material.ACACIA_PRESSURE_PLATE,
                Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
                Material.TORCH,
                Material.TORCHFLOWER_CROP,
                Material.PETRIFIED_OAK_SLAB,
                Material.BARREL,
                Material.BEETROOT_SOUP,
                Material.GILDED_BLACKSTONE,
                Material.BIRCH_BUTTON,
                Material.MILK_BUCKET,
                Material.POWDER_SNOW_BUCKET,
                Material.SNOWBALL,
                Material.MELON_SEEDS,
                Material.BROWN_CARPET,
                Material.LIME_CANDLE,
                Material.COPPER_BLOCK,
                Material.WAXED_OXIDIZED_COPPER_BULB,
                Material.OXIDIZED_CHISELED_COPPER,
                Material.OXIDIZED_COPPER_DOOR,
                Material.OXIDIZED_COPPER_GRATE,
                Material.OXIDIZED_COPPER_TRAPDOOR,
                Material.OXIDIZED_COPPER_BULB,
                Material.OXIDIZED_COPPER_TRAPDOOR,
                Material.OXIDIZED_CUT_COPPER_SLAB,
                Material.OXIDIZED_CUT_COPPER_STAIRS,
                Material.MUDDY_MANGROVE_ROOTS
        });

        lots.put("basic2", new Material[]{
                Material.ACACIA_SAPLING,
                Material.COARSE_DIRT,
                Material.ROOTED_DIRT,
                Material.MUD,
                Material.PODZOL,
                Material.SNOW_BLOCK,
                Material.PIG_SPAWN_EGG,
                Material.TNT_MINECART,
                Material.FURNACE_MINECART,
                Material.AMETHYST_CLUSTER,
                Material.PRIZE_POTTERY_SHERD,
                Material.POLISHED_DEEPSLATE,
                Material.VEX_SPAWN_EGG,
                Material.BIRCH_BUTTON,
                Material.BIRCH_CHEST_BOAT,
                Material.BIRCH_BOAT,
                Material.BREEZE_ROD,
                Material.BLACK_DYE,
                Material.HEAVY_CORE,
                Material.BLACK_BANNER,
                Material.CAVE_VINES,
                Material.CAMPFIRE,
                Material.CHERRY_WALL_HANGING_SIGN,
                Material.WOODEN_SHOVEL,
                Material.COMPASS,
                Material.BLUE_STAINED_GLASS_PANE
        });

        lots.put("hacker", new Material[]{
                Material.TNT, Material.FIREWORK_ROCKET, Material.ENDER_PEARL, Material.SLIME_BLOCK, Material.HONEY_BLOCK
        });

        lots.put("tools", new Material[]{
                Material.IRON_SWORD, Material.DIAMOND_PICKAXE, Material.GOLDEN_SHOVEL, Material.IRON_AXE, Material.SHEARS
        });
    }

    /**
     * Gets a random material from the specified lote.
     *
     * @param loteName the name of the lote
     * @return a random material from the lote, or null if the lote doesn't exist
     */

    public static Material getRandomMaterialFromLote(String loteName) {
        Material[] lote = lots.get(loteName);
        if (lote == null || lote.length == 0) {
            return null; // Return null if the lote doesn't exist or is empty
        }
        return lote[random.nextInt(lote.length)];
    }

    /**
     * Gets all available lote names.
     *
     * @return an array of lote names
     */
    public static String[] getLoteNames() {
        return lots.keySet().toArray(new String[0]);
    }
}
