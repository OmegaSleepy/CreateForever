package org.omega.createforever.datagen.vanilla;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.omega.createforever.CreateForever;
import org.omega.createforever.items.ModItems;
import org.omega.createforever.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider (PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CreateForever.MODID, existingFileHelper);
    }

    @Override
    protected void addTags (HolderLookup.Provider provider) {
        tag(ModTags.Items.CHRISTMAS_HATS)
                .add(ModItems.WHITE_CHRISTMAS_HAT.get())
                .add(ModItems.ORANGE_CHRISTMAS_HAT.get())
                .add(ModItems.MAGENTA_CHRISTMAS_HAT.get())
                .add(ModItems.LIGHT_BLUE_CHRISTMAS_HAT.get())
                .add(ModItems.YELLOW_CHRISTMAS_HAT.get())
                .add(ModItems.LIME_CHRISTMAS_HAT.get())
                .add(ModItems.PINK_CHRISTMAS_HAT.get())
                .add(ModItems.GRAY_CHRISTMAS_HAT.get())
                .add(ModItems.LIGHT_GRAY_CHRISTMAS_HAT.get())
                .add(ModItems.CYAN_CHRISTMAS_HAT.get())
                .add(ModItems.PURPLE_CHRISTMAS_HAT.get())
                .add(ModItems.BLUE_CHRISTMAS_HAT.get())
                .add(ModItems.BROWN_CHRISTMAS_HAT.get())
                .add(ModItems.GREEN_CHRISTMAS_HAT.get())
                .add(ModItems.RED_CHRISTMAS_HAT.get())
                .add(ModItems.BLACK_CHRISTMAS_HAT.get());

        tag(ModTags.Items.LEGACY_HATS)
                .add(ModItems.CAPELA.get())
                .add(ModItems.COOK.get())
                .add(ModItems.CROWN.get())
                .add(ModItems.MAFIA.get())
                .add(ModItems.MASK_P.get())
                .add(ModItems.MASK_S.get())
                .add(ModItems.CHEF.get())
                .add(ModItems.WITCH.get());

        tag(ModTags.Items.HALLOWEEN_HATS)
                .add(ModItems.MASK_P.get())
                .add(ModItems.MASK_S.get())
                .add(ModItems.WITCH.get())
                .add(ModItems.CAT_EARS.get());

        tag(ModTags.Items.CASUAL_HATS)
                .add(ModItems.CAPELA.get())
                .add(ModItems.BARET.get());

        tag(ModTags.Items.CRAFTABLE_HATS)
                .addTag(ModTags.Items.CHRISTMAS_HATS)
                .addTag(ModTags.Items.LEGACY_HATS)
                .addTag(ModTags.Items.HALLOWEEN_HATS)
                .addTag(ModTags.Items.CASUAL_HATS);
    }
}