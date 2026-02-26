package org.omega.createforever.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.MobBucketItem;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
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

        for (var flower: ModBlocks.FLOWERS){
            String name = flower.getId().getPath();

            ModelFile mf = models().cross(
                    name,
                    modLoc("block/" + name)
            ).renderType("cutout");

            simpleBlock(flower.get(), mf);

        }

    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}