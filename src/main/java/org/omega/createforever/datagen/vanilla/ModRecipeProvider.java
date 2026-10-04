package org.omega.createforever.datagen.vanilla;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.omega.createforever.CreateForever;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.datagen.create.LocometalRecipeProvider;
import org.omega.createforever.datagen.create.LocometalWashingRecipeProvider;
import org.omega.createforever.items.ModItems;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
        this.lookupProvider = registries;
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
        LocometalRecipeProvider.buildRecipes(recipeOutput, this.lookupProvider);

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(Items.TUFF),
                        RecipeCategory.BUILDING_BLOCKS, // or RecipeCategory.MISC / BLOCKS
                        ModBlocks.ENRICHED_TUFF.get(),  // Result ItemLike
                        0.4f,                           // Experience
                        100                             // Cooking time in ticks (100 = 5 seconds)
                )
                .unlockedBy("has_tuff", has(Items.TUFF))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "enriching_tuff"));

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BUNDLE)
                .define('#', Items.STRING)
                .define('L', Items.LEATHER)
                .pattern("#")
                .pattern("L")
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SADDLE)
                .define('#', Items.LEATHER)
                .define('i', Items.IRON_INGOT)
                .pattern(" # ")
                .pattern("#i#")
                .unlockedBy("has_leather", has(Items.LEATHER))
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

        hats(recipeOutput);

    }

    private void hats(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CROWN)
                .define('L', Items.GOLD_INGOT)
                .define('#', Items.DIAMOND)
                .pattern("L#L")
                .pattern("LLL")
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.COOK)
                .define('#', Items.WHITE_WOOL)
                .define('S', Items.STRING)
                .pattern("#")
                .pattern("S")
                .pattern("#")
                .unlockedBy("has_wool", has(Items.WHITE_WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.MASK_P)
                .define('#', Items.CARVED_PUMPKIN)
                .define('S', Items.STRING)
                .pattern(" S ")
                .pattern("S S")
                .pattern(" # ")
                .unlockedBy("has_carved_pumpkin", has(Items.CARVED_PUMPKIN))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.MASK_S)
                .define('#', Items.SLIME_BLOCK)
                .define('S', Items.STRING)
                .pattern(" S ")
                .pattern("S S")
                .pattern(" # ")
                .unlockedBy("has_slime", has(Items.SLIME_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CAPELA)
                .define('#', Items.WHEAT)
                .define('S', Items.HAY_BLOCK)
                .pattern(" # ")
                .pattern("#S#")
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.MAFIA)
                .define('#', Items.PURPLE_WOOL)
                .define('R', Items.YELLOW_DYE)
                .define('S', Items.STRING)
                .pattern("SRS")
                .pattern("###")
                .unlockedBy("has_purple_wool", has(Items.PURPLE_WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.WITCH)
                .define('#', Items.GREEN_WOOL)
                .define('E', Items.EMERALD)
                .pattern(" # ")
                .pattern("#E#")
                .pattern("###")
                .unlockedBy("has_green_wool", has(Items.GREEN_WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CHEF)
                .define('#', Items.BLACK_WOOL)
                .define('B', Items.BROWN_DYE)
                .define('R', Items.RED_DYE)
                .pattern(" # ")
                .pattern("R#R")
                .pattern(" B ")
                .unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CAT_EARS)
                .define('#', Items.BLACK_WOOL)
                .define('B', Items.PINK_DYE)
                .pattern("# #")
                .pattern("B B")
                .unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.BARET)
                .define('#', Items.BLACK_WOOL)
                .define('F', Items.FEATHER)
                .pattern("F  ")
                .pattern("###")
                .unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
            .save(recipeOutput);
    }

}