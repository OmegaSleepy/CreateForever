package org.omega.createforever.util;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.omega.createforever.CreateForever;

import java.util.List;
import java.util.Map;

public class DynamicRecipeHelper {

    public static boolean buildDynamicShapedRecipe(
            RecipeOutput output,
            HolderGetter<Item> itemGetter,
            RecipeCategory category,
            String outputItemIdStr,
            int count,
            List<String> pattern,
            Map<Character, String> symbolMap
    ) {
        // 1. Safe Output Item Lookup
        ResourceLocation outputId = ResourceLocation.parse(outputItemIdStr);
        ResourceKey<Item> outputKey = ResourceKey.create(Registries.ITEM, outputId);
        var optionalOutputHolder = itemGetter.get(outputKey);

        if (optionalOutputHolder.isEmpty()) {
            System.out.println("[DynamicRecipeHelper] Skipped Shaped (item not in registry): " + outputItemIdStr);
            return false;
        }

        var outputHolder = optionalOutputHolder.get();
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, outputHolder.value(), count);

        // 2. Set pattern
        for (String line : pattern) {
            builder.pattern(line);
        }

        // 3. Resolve ingredients dynamically (handling tags with '#')
        for (Map.Entry<Character, String> entry : symbolMap.entrySet()) {
            String ingredientStr = entry.getValue();

            if (ingredientStr.startsWith("#")) {
                ResourceLocation tagId = ResourceLocation.parse(ingredientStr.substring(1));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagId);
                builder.define(entry.getKey(), Ingredient.of(tagKey));
            } else {
                ResourceLocation ingredientId = ResourceLocation.parse(ingredientStr);
                ResourceKey<Item> ingredientKey = ResourceKey.create(Registries.ITEM, ingredientId);
                var optionalIngredient = itemGetter.get(ingredientKey);

                if (optionalIngredient.isEmpty()) {
                    System.out.println("[DynamicRecipeHelper] Skipped Shaped (ingredient missing): " + ingredientStr);
                    return false;
                }

                builder.define(entry.getKey(), Ingredient.of(optionalIngredient.get().value()));
            }
        }

        Criterion<InventoryChangeTrigger.TriggerInstance> unlockCriterion =
                InventoryChangeTrigger.TriggerInstance.hasItems(outputHolder.value());

        builder.unlockedBy("has_" + outputId.getPath(), unlockCriterion)
                .save(output, ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "locometal/" + outputId.getPath() + "_shaped"));

        return true;
    }

    public static boolean buildDynamicShapelessRecipe(
            RecipeOutput output,
            HolderGetter<Item> itemGetter,
            RecipeCategory category,
            String outputItemIdStr,
            int count,
            List<String> ingredients
    ) {
        // 1. Safe Output Item Lookup
        ResourceLocation outputId = ResourceLocation.parse(outputItemIdStr);
        ResourceKey<Item> outputKey = ResourceKey.create(Registries.ITEM, outputId);
        var optionalOutputHolder = itemGetter.get(outputKey);

        if (optionalOutputHolder.isEmpty()) {
            System.out.println("[DynamicRecipeHelper] Skipped Shapeless (item not in registry): " + outputItemIdStr);
            return false;
        }

        var outputHolder = optionalOutputHolder.get();
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, outputHolder.value(), count);

        // 2. Safe Ingredient Lookups
        for (String ingredientStr : ingredients) {
            if (ingredientStr.startsWith("#")) {
                ResourceLocation tagId = ResourceLocation.parse(ingredientStr.substring(1));
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagId);
                builder.requires(Ingredient.of(tagKey));
            } else {
                ResourceLocation ingredientId = ResourceLocation.parse(ingredientStr);
                ResourceKey<Item> ingredientKey = ResourceKey.create(Registries.ITEM, ingredientId);
                var optionalIngredient = itemGetter.get(ingredientKey);

                if (optionalIngredient.isEmpty()) {
                    System.out.println("[DynamicRecipeHelper] Skipped Shapeless (ingredient missing): " + ingredientStr);
                    return false;
                }

                builder.requires(Ingredient.of(optionalIngredient.get().value()));
            }
        }

        // 3. Save Recipe
        Criterion<InventoryChangeTrigger.TriggerInstance> unlockCriterion =
                InventoryChangeTrigger.TriggerInstance.hasItems(outputHolder.value());

        builder.unlockedBy("has_" + outputId.getPath(), unlockCriterion)
                .save(output, ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "locometal/" + outputId.getPath() + "_shapeless"));

        return true;
    }
}