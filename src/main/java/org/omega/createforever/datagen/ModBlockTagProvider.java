package org.omega.createforever.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import org.omega.createforever.CreateForever;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.util.ModTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateForever.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Blocks.FLOWERS)
                .add(ModBlocks.AMARYLLIS.get())
                .add(ModBlocks.BLUEBELL.get())
                .add(ModBlocks.CHRYSANTHEMUM.get())
                .add(ModBlocks.ORCHID.get())
                .add(ModBlocks.VIOLET.get())
        ;

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.CONCRETE_POWDER.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CONCRETE.get());
    }

}
