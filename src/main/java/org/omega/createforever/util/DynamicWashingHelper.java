package org.omega.createforever.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public class DynamicWashingHelper {

    /**
     * Builds a Create Washing recipe JsonObject directly without needing RecipeSerializers.
     */
    public static JsonObject createWashingJson(
            HolderGetter<Item> itemGetter,
            String inputItemIdOrTag,
            List<OutputResult> results
    ) {
        // 1. Validate Input (Item or Tag)
        JsonObject ingredientObj = new JsonObject();

        if (inputItemIdOrTag.startsWith("#")) {
            String tagId = inputItemIdOrTag.substring(1);
            ingredientObj.addProperty("tag", tagId);
        } else {
            ResourceLocation inputId = ResourceLocation.parse(inputItemIdOrTag);
            ResourceKey<Item> inputKey = ResourceKey.create(Registries.ITEM, inputId);

            if (itemGetter.get(inputKey).isEmpty()) {
                System.out.println("[DynamicWashingHelper] Skipped Washing (input missing): " + inputItemIdOrTag);
                return null;
            }
            ingredientObj.addProperty("item", inputItemIdOrTag);
        }

        // 2. Validate & Build Results Array
        JsonArray resultsArray = new JsonArray();

        for (OutputResult res : results) {
            ResourceLocation resultId = ResourceLocation.parse(res.id());
            ResourceKey<Item> resultKey = ResourceKey.create(Registries.ITEM, resultId);

            if (itemGetter.get(resultKey).isEmpty()) {
                System.out.println("[DynamicWashingHelper] Skipped Washing (result missing): " + res.id());
                return null;
            }

            JsonObject resultObj = new JsonObject();
            if (res.chance() < 1.0f) {
                resultObj.addProperty("chance", res.chance());
            }
            resultObj.addProperty("id", res.id());

            if (res.count() > 1) {
                resultObj.addProperty("count", res.count());
            }

            resultsArray.add(resultObj);
        }

        // 3. Assemble Full Recipe JSON Object
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "create:splashing");

        JsonArray ingredientsArray = new JsonArray();
        ingredientsArray.add(ingredientObj);
        recipeJson.add("ingredients", ingredientsArray);

        recipeJson.add("results", resultsArray);

        return recipeJson;
    }

    /**
     * DTO for custom output results.
     */
    public record OutputResult(String id, int count, float chance) {
        public OutputResult(String id, float chance) {
            this(id, 1, chance);
        }

        public OutputResult(String id) {
            this(id, 1, 1.0f);
        }
    }
}