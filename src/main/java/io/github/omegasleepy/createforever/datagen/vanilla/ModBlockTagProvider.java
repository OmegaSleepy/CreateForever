package io.github.omegasleepy.createforever.datagen.vanilla;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import io.github.omegasleepy.createforever.CreateForever;
import io.github.omegasleepy.createforever.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateForever.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.CONCRETE_POWDER.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CONCRETE.get())
                .add(ModBlocks.ENRICHED_TUFF.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.SEWING_TABLE.get());
    }

}
