package io.github.omegasleepy.createforever.datagen.forever;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import io.github.omegasleepy.createforever.CreateForever;
import io.github.omegasleepy.createforever.items.ModItems;
import io.github.omegasleepy.createforever.recipe.SewingRecipe;
import io.github.omegasleepy.createforever.util.ModTags;

import java.util.Arrays;
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
        add(output, "crown", ModItems.CROWN.get(),
                repeated(Items.GOLD_INGOT, 3, Items.RED_WOOL, Items.DIAMOND));

        add(output, "cook", ModItems.COOK.get(),
                ingredients(Items.WHITE_WOOL, Items.WHITE_WOOL, ModItems.SEWING_SPOOL.get()));

        add(output, "mask_pumpkin", ModItems.MASK_P.get(),
                ingredients(Items.CARVED_PUMPKIN, Items.STRING, ModItems.SEWING_SPOOL.get()));
        add(output, "mask_slime", ModItems.MASK_S.get(),
                ingredients(Items.SLIME_BALL, Items.STRING, ModItems.SEWING_SPOOL.get()));

        add(output, "capela", ModItems.CAPELLA.get(),
                repeated(Items.WHEAT, 3, Items.YELLOW_WOOL, Items.STRING));

        add(output, "mafia", ModItems.MAFIA.get(),
                ingredients(Items.PURPLE_WOOL, Items.PURPLE_WOOL, Items.LEATHER, ModItems.SEWING_SPOOL.get()));

        add(output, "witch", ModItems.WITCH.get(),
                ingredients(Items.GREEN_WOOL, Items.BLACK_WOOL, Items.LEATHER, ModItems.SEWING_SPOOL.get()));

        add(output, "chef", ModItems.TOP_HAT.get(),
                ingredients(Items.BLACK_WOOL, Items.BLACK_WOOL, Items.RED_WOOL, ModItems.SEWING_SPOOL.get()));

        add(output, "cat_ears", ModItems.CAT_EARS.get(),
                ingredients(Items.BLACK_WOOL, Items.PINK_WOOL, Items.LEATHER, Items.STRING));

        add(output, "baret", ModItems.BERET.get(),
                ingredients(Items.BLACK_WOOL, Items.BLACK_WOOL, Items.FEATHER, ModItems.SEWING_SPOOL.get()));

        add(output, "antler", ModItems.DEER_ANTLERS.get(),
                ingredients(Items.LEATHER, Items.STICK, Items.STICK, Items.STRING));
        add(output, "antler_rodolph", ModItems.DEER_ANTLERS_RUDOLPH.get(),
                ingredients(Items.LEATHER, Items.STICK, Items.STICK, Items.RED_WOOL));
        add(output, "antler_rodolph_alternative", ModItems.DEER_ANTLERS_RUDOLPH.get(),
                ingredients(ModItems.DEER_ANTLERS.get(), Items.RED_WOOL, ModItems.SEWING_SPOOL.get()));

        add(output, "elf_hat", ModItems.ELF_HAT.get(),
                ingredients(Items.RED_WOOL, Items.GREEN_WOOL, Items.BELL, ModItems.SEWING_SPOOL.get()));

        for (Map.Entry<String, Item> entry : COLOR_WOOL.entrySet()) {
            String color = entry.getKey();
            String itemName = "christmas_" + color + "_hat";
            Item hat = ModItems.ITEMS.getEntries().stream()
                    .filter(item -> item.getId().getPath().equals(itemName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Missing hat item: " + itemName))
                    .get();

            add(output, itemName, hat,
                    ingredients(entry.getValue(), entry.getValue(), Items.WHITE_WOOL, ModItems.SEWING_SPOOL.get()));
            add(output, itemName + "_from_dyeing", hat,
                    Ingredient.of(ModTags.Items.CHRISTMAS_HATS_DYEABLE),
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

    private static Ingredient[] ingredients(Item... items) {
        return Arrays.stream(items).map(Ingredient::of).toArray(Ingredient[]::new);
    }

    private static void add(RecipeOutput output, String name, Item result, Ingredient... ingredients) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "sewing/" + name);
        output.accept(id, new SewingRecipe(List.of(ingredients), new ItemStack(result)), null);
    }
}