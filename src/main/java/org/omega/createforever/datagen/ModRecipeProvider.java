package org.omega.createforever.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.items.ModItems;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    Map<Item, Block> colorToConcrete = Map.ofEntries(
            Map.entry(Items.WHITE_DYE, Blocks.WHITE_CONCRETE_POWDER),
            Map.entry(Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_CONCRETE_POWDER),
            Map.entry(Items.GRAY_DYE, Blocks.GRAY_CONCRETE_POWDER),
            Map.entry(Items.BLACK_DYE, Blocks.BLACK_CONCRETE_POWDER),

            Map.entry(Items.BROWN_DYE, Blocks.BROWN_CONCRETE_POWDER),
            Map.entry(Items.RED_DYE, Blocks.RED_CONCRETE_POWDER),
            Map.entry(Items.ORANGE_DYE, Blocks.ORANGE_CONCRETE_POWDER),
            Map.entry(Items.YELLOW_DYE, Blocks.YELLOW_CONCRETE_POWDER),

            Map.entry(Items.LIME_DYE, Blocks.LIME_CONCRETE_POWDER),
            Map.entry(Items.GREEN_DYE, Blocks.GREEN_CONCRETE_POWDER),
            Map.entry(Items.CYAN_DYE, Blocks.CYAN_CONCRETE_POWDER),
            Map.entry(Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_CONCRETE_POWDER),

            Map.entry(Items.BLUE_DYE, Blocks.BLUE_CONCRETE_POWDER),
            Map.entry(Items.PURPLE_DYE, Blocks.PURPLE_CONCRETE_POWDER),
            Map.entry(Items.MAGENTA_DYE, Blocks.MAGENTA_CONCRETE_POWDER),
            Map.entry(Items.PINK_DYE, Blocks.PINK_CONCRETE_POWDER)
    );


    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CARD_PACK, 1)
                .pattern("?#!")
                .pattern("?L!")
                .pattern("?#!")
                .define('?', Items.BLACK_DYE)
                .define('!', Items.RED_DYE)
                .define('#', Items.PAPER)
                .define('L', Items.SLIME_BALL)
                .unlockedBy("paper", has(Items.PAPER))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CONCRETE_POWDER.get(), 8)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .unlockedBy("has_concrete_stuff", has(Blocks.SAND))
                .save(recipeOutput);

        for (Map.Entry<Item, Block> entry : colorToConcrete.entrySet()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, entry.getValue(), 8)
                    .pattern("CCC")
                    .pattern("CDC")
                    .pattern("CCC")
                    .define('C', ModBlocks.CONCRETE_POWDER)
                    .define('D', entry.getKey())
                    .unlockedBy("has_concrete_powder", has(entry.getKey()))
                    .save(recipeOutput, entry.getValue().getDescriptionId().replace("block.minecraft.", "concrete_powder."));
        }

    }

}