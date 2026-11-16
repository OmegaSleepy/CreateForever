package org.omega.createforever.datagen.vanilla;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.omega.createforever.CreateForever;
import org.omega.createforever.blocks.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateForever.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.CONCRETE_POWDER);
        blockWithItem(ModBlocks.CONCRETE);
        blockWithItem(ModBlocks.ENRICHED_TUFF);
        simpleBlockWithItem(ModBlocks.SEWING_TABLE.get(),
                models().cubeAll("sewing_machine", ResourceLocation.withDefaultNamespace("block/loom_side")));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}