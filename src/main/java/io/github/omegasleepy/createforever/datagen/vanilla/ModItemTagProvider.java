package io.github.omegasleepy.createforever.datagen.vanilla;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import io.github.omegasleepy.createforever.CreateForever;
import io.github.omegasleepy.createforever.util.HatTagContents;
import io.github.omegasleepy.createforever.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider (PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CreateForever.MODID, existingFileHelper);
    }

    @Override
    protected void addTags (HolderLookup.Provider provider) {
        tag(ModTags.Items.CHRISTMAS_HATS_DYEABLE)
                .add(HatTagContents.CHRISTMAS_HATS_DYEABLE.toArray(Item[]::new));
        tag(ModTags.Items.CHRISTMAS_HATS)
                .add(HatTagContents.CHRISTMAS_HATS_OTHER.toArray(Item[]::new))
                .addTag(ModTags.Items.CHRISTMAS_HATS_DYEABLE);

        tag(ModTags.Items.LEGACY_HATS).add(HatTagContents.GROUPS.get(ModTags.Items.LEGACY_HATS).toArray(Item[]::new));
        tag(ModTags.Items.HALLOWEEN_HATS).add(HatTagContents.GROUPS.get(ModTags.Items.HALLOWEEN_HATS).toArray(Item[]::new));
        tag(ModTags.Items.CASUAL_HATS).add(HatTagContents.GROUPS.get(ModTags.Items.CASUAL_HATS).toArray(Item[]::new));
        tag(ModTags.Items.FUN_HATS).add(HatTagContents.GROUPS.get(ModTags.Items.FUN_HATS).toArray(Item[]::new));

        tag(ModTags.Items.CRAFTABLE_HATS)
                .addTag(ModTags.Items.CHRISTMAS_HATS)
                .addTag(ModTags.Items.LEGACY_HATS)
                .addTag(ModTags.Items.HALLOWEEN_HATS)
                .addTag(ModTags.Items.CASUAL_HATS)
                .addTag(ModTags.Items.FUN_HATS);
    }
}