package org.omega.createforever.datagen;

import net.minecraft.client.model.Model;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.omega.createforever.Createforever;
import org.omega.createforever.items.ModItems;

import static org.omega.createforever.Createforever.MODID;
import static org.omega.createforever.items.ModItems.RANKS;
import static org.omega.createforever.items.ModItems.SUITS;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile template = getExistingFile(modLoc("item/playing_card"));

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                String cardId = suit.toLowerCase() + "_" + rank.toLowerCase();
                ItemModelBuilder builder = getBuilder(cardId);

                builder.parent(template);
                builder.texture("missing", modLoc("block/" + "concrete"));
            }
        }
    }
}