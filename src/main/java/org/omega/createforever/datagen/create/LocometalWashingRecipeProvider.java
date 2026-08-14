package org.omega.createforever.datagen.create;

import com.google.gson.JsonObject;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.omega.createforever.CreateForever;
import org.omega.createforever.util.DynamicWashingHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LocometalWashingRecipeProvider implements DataProvider {

    private final PackOutput.PathProvider recipePathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public LocometalWashingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.recipePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return this.registries.thenCompose(provider -> {
            HolderGetter<Item> itemGetter = provider.lookupOrThrow(Registries.ITEM);
            List<CompletableFuture<?>> futures = new ArrayList<>();

            for (Map.Entry<String, String> entry : LOCOMETAL_STYLE_ITEM_SUFFIXES.entrySet()) {
                String stylePath = entry.getKey();
                String itemSuffix = entry.getValue();

                // Input Tag & Target Output Item
                String inputTag = "#railways:palettes/dye_groups/" + stylePath;
                String outputItem = "railways:" + itemSuffix;

                // Build JSON Object
                JsonObject json = DynamicWashingHelper.createWashingJson(
                        itemGetter,
                        inputTag,
                        List.of(new DynamicWashingHelper.OutputResult(outputItem, 1, 1.0f))
                );

                if (json != null) {
                    ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                            CreateForever.MODID,
                            "splashing/" + itemSuffix
                    );

                    Path path = this.recipePathProvider.json(recipeId);

                    // Directly write JSON file bypassing vanilla Recipe.CODEC registry checks
                    futures.add(DataProvider.saveStable(cache, json, path));
                }
            }

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    @Override
    public String getName() {
        return "Create Forever - Locometal Washing Recipes";
    }

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
            Map.entry("hazard_stripes_diagonal_black", "hazard_stripes_diagonal_on_black"),
            Map.entry("hazard_stripes_chevron_black", "hazard_stripes_chevron_on_black"),
            Map.entry("hazard_stripes_diagonal_white", "hazard_stripes_diagonal_on_white"),
            Map.entry("hazard_stripes_chevron_white", "hazard_stripes_chevron_on_white")
    );
}