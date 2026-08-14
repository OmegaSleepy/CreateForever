package org.omega.createforever.datagen.create;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import org.omega.createforever.util.DynamicRecipeHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LocometalRecipeProvider {

    private static final Map<String, String> COLOR_MAP = Map.ofEntries(
            Map.entry("red", "minecraft:red_dye"),
            Map.entry("orange", "minecraft:orange_dye"),
            Map.entry("yellow", "minecraft:yellow_dye"),
            Map.entry("lime", "minecraft:lime_dye"),
            Map.entry("green", "minecraft:green_dye"),
            Map.entry("light_blue", "minecraft:light_blue_dye"),
            Map.entry("cyan", "minecraft:cyan_dye"),
            Map.entry("blue", "minecraft:blue_dye"),
            Map.entry("purple", "minecraft:purple_dye"),
            Map.entry("magenta", "minecraft:magenta_dye"),
            Map.entry("pink", "minecraft:pink_dye"),
            Map.entry("brown", "minecraft:brown_dye"),
            Map.entry("black", "minecraft:black_dye"),
            Map.entry("gray", "minecraft:gray_dye"),
            Map.entry("light_gray", "minecraft:light_gray_dye"),
            Map.entry("white", "minecraft:white_dye")
    );

    // Map style path -> actual item registry path suffix
    public static final Map<String, String> LOCOMETAL_STYLE_ITEM_SUFFIXES = Map.ofEntries(
            // Base Styles
            Map.entry("riveted", "riveted_locometal"),
            Map.entry("slashed", "slashed_locometal"),
            Map.entry("brass_wrapped_slashed", "brass_wrapped_locometal"),
            Map.entry("copper_wrapped_slashed", "copper_wrapped_locometal"),
            Map.entry("iron_wrapped_slashed", "iron_wrapped_locometal"),
            Map.entry("flat_riveted", "flat_riveted_locometal"),
            Map.entry("flat_slashed", "flat_slashed_locometal"),
            Map.entry("plated", "plated_locometal"),
            Map.entry("pillar", "locometal_pillar"),
            Map.entry("vent", "locometal_vent"),
            Map.entry("treadplate", "locometal_treadplate"),
            Map.entry("mesh", "locometal_mesh"),
            Map.entry("small_steps", "locometal_small_steps"),
            Map.entry("boiler_rivets", "locometal_boiler_rivets"),

            // Smokeboxes
            Map.entry("smokebox", "locometal_smokebox"),
            Map.entry("brass_wrapped_smokebox", "brass_wrapped_locometal_smokebox"),
            Map.entry("copper_wrapped_smokebox", "copper_wrapped_locometal_smokebox"),
            Map.entry("iron_wrapped_smokebox", "iron_wrapped_locometal_smokebox"),

            // Boilers
            Map.entry("boiler", "locometal_boiler"),
            Map.entry("brass_wrapped_boiler", "brass_wrapped_locometal_boiler"),
            Map.entry("copper_wrapped_boiler", "copper_wrapped_locometal_boiler"),
            Map.entry("iron_wrapped_boiler", "iron_wrapped_locometal_boiler"),

            // Utility Blocks
            Map.entry("flywheel", "locometal_flywheel"),
            Map.entry("trapdoor", "locometal_trapdoor"),
            Map.entry("end_ladder", "locometal_end_ladder"),
            Map.entry("rung_ladder", "locometal_rung_ladder"),

            // Doors
            Map.entry("hinged_door", "hinged_locometal_door"),
            Map.entry("sliding_door", "sliding_locometal_door"),
            Map.entry("folding_door", "folding_locometal_door"),

            // Windows
            Map.entry("round_pane_window", "round_pane_locometal_window"),
            Map.entry("single_pane_window", "single_pane_locometal_window"),
            Map.entry("two_pane_window", "two_pane_locometal_window"),
            Map.entry("four_pane_window", "four_pane_locometal_window"),

            // Hazard Stripes
            //railways:red_hazard_stripes_chevron_on_black
            Map.entry("hazard_stripes_diagonal_black", "hazard_stripes_diagonal_on_black"),
            Map.entry("hazard_stripes_chevron_black", "hazard_stripes_chevron_on_black"),
            Map.entry("hazard_stripes_diagonal_white", "hazard_stripes_diagonal_on_white"),
            Map.entry("hazard_stripes_chevron_white", "hazard_stripes_chevron_on_white")
    );

    public static void buildRecipes(RecipeOutput recipeOutput, CompletableFuture<HolderLookup.Provider> registries) {
        HolderGetter<Item> itemGetter;
        try {
            itemGetter = registries.get().lookupOrThrow(Registries.ITEM);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : LOCOMETAL_STYLE_ITEM_SUFFIXES.entrySet()) {
            String stylePath = entry.getKey();
            String itemSuffix = entry.getValue();
            String tagString = "#railways:palettes/dye_groups/" + stylePath;

            COLOR_MAP.forEach((colorName, dyeItemStr) -> {
                String outputItem = "railways:%s_%s".formatted(colorName, itemSuffix);

                DynamicRecipeHelper.buildDynamicShapelessRecipe(
                        recipeOutput,
                        itemGetter,
                        RecipeCategory.BUILDING_BLOCKS,
                        outputItem,
                        1,
                        List.of(dyeItemStr, tagString)
                );

                DynamicRecipeHelper.buildDynamicShapedRecipe(
                        recipeOutput,
                        itemGetter,
                        RecipeCategory.BUILDING_BLOCKS,
                        outputItem,
                        8,
                        List.of(
                                "###",
                                "#D#",
                                "###"
                        ),
                        Map.of(
                                '#', tagString,
                                'D', dyeItemStr
                        )
                );
            });
        }
    }
}