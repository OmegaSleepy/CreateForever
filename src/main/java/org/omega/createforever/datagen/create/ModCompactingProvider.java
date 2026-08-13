package org.omega.createforever.datagen.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModCompactingProvider extends CompactingRecipeGen {

    public final GeneratedRecipe azurine = create(
        "azurine_crafting",
        b -> b
                .require(Items.TUFF)
                .require(AllItems.ZINC_NUGGET)
                .requiresHeat(HeatCondition.HEATED)
                .output(AllPaletteStoneTypes.ASURINE.baseBlock.get())
    );

    public final GeneratedRecipe ochre = create(
        "ochre_crafting",
        b -> b
                .require(Items.CLAY_BALL)
                .require(Items.GOLD_NUGGET)
                .requiresHeat(HeatCondition.HEATED)
                .output(AllPaletteStoneTypes.OCHRUM.baseBlock.get())
    );

    public final GeneratedRecipe crimsite = create(
        "crimsite_crafting",
        b -> b
                .require(Items.NETHERRACK)
                .require(Items.IRON_NUGGET)
                .requiresHeat(HeatCondition.HEATED)
                .output(AllPaletteStoneTypes.CRIMSITE.baseBlock.get())
    );

    public final GeneratedRecipe veridium = create(
        "veridium_crafting",
        b -> b
                .require(Items.QUARTZ)
                .require(AllItems.COPPER_NUGGET)
                .requiresHeat(HeatCondition.HEATED)
                .output(AllPaletteStoneTypes.VERIDIUM.baseBlock.get())
    );

    public final GeneratedRecipe basalt = create(
            "basalt_crafting",
            b -> b
                    .require(Items.COBBLESTONE)
                    .require(ItemTags.COALS)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(Items.BASALT)
    );

    public ModCompactingProvider (PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {

        super(output, registries, defaultNamespace);
    }
}
