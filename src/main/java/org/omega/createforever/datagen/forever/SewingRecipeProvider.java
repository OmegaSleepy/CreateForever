package org.omega.createforever.datagen.forever;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.omega.createforever.CreateForever;
import org.omega.createforever.items.ModItems;
import org.omega.createforever.recipe.SewingRecipe;
import org.omega.createforever.util.ModTags;

import java.util.List;
import java.util.Map;

public final class SewingRecipeProvider {
    private static final Map<String, Item> COLOR_WOOL = Map.ofEntries(
            Map.entry("white", Blocks.WHITE_WOOL.asItem()),
            Map.entry("light_gray", Blocks.LIGHT_GRAY_WOOL.asItem()),
            Map.entry("gray", Blocks.GRAY_WOOL.asItem()),
            Map.entry("black", Blocks.BLACK_WOOL.asItem()),
            Map.entry("brown", Blocks.BROWN_WOOL.asItem()),
            Map.entry("red", Blocks.RED_WOOL.asItem()),
            Map.entry("orange", Blocks.ORANGE_WOOL.asItem()),
            Map.entry("yellow", Blocks.YELLOW_WOOL.asItem()),
            Map.entry("lime", Blocks.LIME_WOOL.asItem()),
            Map.entry("green", Blocks.GREEN_WOOL.asItem()),
            Map.entry("cyan", Blocks.CYAN_WOOL.asItem()),
            Map.entry("light_blue", Blocks.LIGHT_BLUE_WOOL.asItem()),
            Map.entry("blue", Blocks.BLUE_WOOL.asItem()),
            Map.entry("purple", Blocks.PURPLE_WOOL.asItem()),
            Map.entry("magenta", Blocks.MAGENTA_WOOL.asItem()),
            Map.entry("pink", Blocks.PINK_WOOL.asItem())
    );

    private static final Map<String, Item> COLOR_DYE = Map.ofEntries(
            Map.entry("white", Items.WHITE_DYE),
            Map.entry("light_gray", Items.LIGHT_GRAY_DYE),
            Map.entry("gray", Items.GRAY_DYE),
            Map.entry("black", Items.BLACK_DYE),
            Map.entry("brown", Items.BROWN_DYE),
            Map.entry("red", Items.RED_DYE),
            Map.entry("orange", Items.ORANGE_DYE),
            Map.entry("yellow", Items.YELLOW_DYE),
            Map.entry("lime", Items.LIME_DYE),
            Map.entry("green", Items.GREEN_DYE),
            Map.entry("cyan", Items.CYAN_DYE),
            Map.entry("light_blue", Items.LIGHT_BLUE_DYE),
            Map.entry("blue", Items.BLUE_DYE),
            Map.entry("purple", Items.PURPLE_DYE),
            Map.entry("magenta", Items.MAGENTA_DYE),
            Map.entry("pink", Items.PINK_DYE)
    );

    private SewingRecipeProvider() {
    }

    public static void buildRecipes(RecipeOutput output) {
        add(output, "crown", ModItems.CROWN.get(), repeated(Items.GOLD_INGOT, 5, Items.DIAMOND));
        add(output, "cook", ModItems.COOK.get(), repeated(Items.WHITE_WOOL, 2, Items.STRING));
        add(output, "mask_pumpkin", ModItems.MASK_P.get(),
                repeated(Items.STRING, 3, Items.CARVED_PUMPKIN));
        add(output, "mask_slime", ModItems.MASK_S.get(),
                repeated(Items.STRING, 3, Items.SLIME_BLOCK));
        add(output, "capela", ModItems.CAPELA.get(),
                repeated(Items.WHEAT, 3, Items.HAY_BLOCK));
        add(output, "mafia", ModItems.MAFIA.get(),
                repeated(Items.PURPLE_WOOL, 3, Items.STRING, Items.STRING, Items.YELLOW_DYE));
        add(output, "witch", ModItems.WITCH.get(),
                repeated(Items.GREEN_WOOL, 5, Items.EMERALD));
        add(output, "chef", ModItems.CHEF.get(),
                repeated(Items.BLACK_WOOL, 3, Items.RED_DYE, Items.RED_DYE, Items.BROWN_DYE));
        add(output, "cat_ears", ModItems.CAT_EARS.get(),
                repeated(Items.BLACK_WOOL, 2, Items.PINK_DYE, Items.PINK_DYE));
        add(output, "baret", ModItems.BARET.get(),
                repeated(Items.BLACK_WOOL, 3, Items.FEATHER));

        for (Map.Entry<String, Item> entry : COLOR_WOOL.entrySet()) {
            String color = entry.getKey();
            String itemName = "christmas_" + color + "_hat";
            Item hat = ModItems.ITEMS.getEntries().stream()
                    .filter(item -> item.getId().getPath().equals(itemName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Missing hat item: " + itemName))
                    .get();

            add(output, itemName, hat, repeated(Items.WHITE_WOOL, 4,
                    entry.getValue(), entry.getValue(), entry.getValue()));
            add(output, itemName + "_from_dyeing", hat,
                    Ingredient.of(ModTags.Items.CHRISTMAS_HATS),
                    Ingredient.of(COLOR_DYE.get(color)));
        }
    }

    private static Ingredient[] repeated(Item item, int count, Item... additionalItems) {
        Ingredient[] ingredients = new Ingredient[count + additionalItems.length];
        for (int i = 0; i < count; i++) {
            ingredients[i] = Ingredient.of(item);
        }
        for (int i = 0; i < additionalItems.length; i++) {
            ingredients[count + i] = Ingredient.of(additionalItems[i]);
        }
        return ingredients;
    }

    private static Ingredient[] repeated(Item item, int count, Item second) {
        return repeated(item, count, new Item[]{second});
    }

    private static Ingredient[] repeated(Item item, int count, Item second, Item third) {
        return repeated(item, count, new Item[]{second, third});
    }

    private static void add(RecipeOutput output, String name, Item result, Ingredient... ingredients) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "sewing/" + name);
        output.accept(id, new SewingRecipe(List.of(ingredients), new ItemStack(result)), null);
    }
}
