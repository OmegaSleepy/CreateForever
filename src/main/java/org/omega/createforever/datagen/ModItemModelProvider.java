package org.omega.createforever.datagen;

import com.mojang.logging.LogUtils;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import static org.omega.createforever.CreateForever.MODID;
import static org.omega.createforever.items.ModItems.RANKS;
import static org.omega.createforever.items.ModItems.SUITS;

public class ModItemModelProvider extends ItemModelProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

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

                LOGGER.info("Registering {}", cardId);

                builder.parent(template);
                builder.texture("missing", modLoc("item/card/" + cardId));
            }
        }
    }
}