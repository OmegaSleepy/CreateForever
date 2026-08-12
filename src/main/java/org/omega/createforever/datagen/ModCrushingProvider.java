package org.omega.createforever.datagen;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.tterrag.registrate.providers.RegistrateGenericProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModCrushingProvider extends CrushingRecipeGen {

    public final GeneratedRecipe enrichedTuff = create(
            "enriched_tuff_crushing",
            b -> b.require(ModBlocks.ENRICHED_TUFF)
                    .output(0.5f, Items.IRON_NUGGET)
                    .output(0.5f, Items.GOLD_NUGGET)
                    .output(0.6f, com.simibubi.create.AllItems.COPPER_NUGGET.asStack())
                    .output(0.6f, AllItems.ZINC_NUGGET.asStack())
                    .duration(350)
    );

    public ModCrushingProvider (PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

}
