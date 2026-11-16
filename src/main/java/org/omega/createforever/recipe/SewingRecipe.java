package org.omega.createforever.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public final class SewingRecipe implements CraftingRecipe {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public SewingRecipe(List<Ingredient> ingredients, ItemStack result) {
        if (ingredients.isEmpty() || ingredients.size() > 9) {
            throw new IllegalArgumentException("A sewing recipe must have between 1 and 9 ingredients");
        }
        this.ingredients = NonNullList.copyOf(ingredients);
        this.result = result.copy();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        List<ItemStack> stacks = input.items().stream().filter(stack -> !stack.isEmpty()).toList();
        return stacks.size() == ingredients.size() && canAssignIngredients(stacks, ingredients);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return ingredients.size() <= width * height;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SEWING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.SEWING.get();
    }

    private static boolean canAssignIngredients(List<ItemStack> stacks, List<Ingredient> ingredients) {
        boolean[] used = new boolean[stacks.size()];
        return assignIngredient(0, stacks, ingredients, used);
    }

    private static boolean assignIngredient(int ingredientIndex, List<ItemStack> stacks,
                                            List<Ingredient> ingredients, boolean[] used) {
        if (ingredientIndex == ingredients.size()) {
            return true;
        }

        Ingredient ingredient = ingredients.get(ingredientIndex);
        for (int stackIndex = 0; stackIndex < stacks.size(); stackIndex++) {
            if (!used[stackIndex] && ingredient.test(stacks.get(stackIndex))) {
                used[stackIndex] = true;
                if (assignIngredient(ingredientIndex + 1, stacks, ingredients, used)) {
                    return true;
                }
                used[stackIndex] = false;
            }
        }
        return false;
    }

    public static final class Serializer implements RecipeSerializer<SewingRecipe> {
        private static final MapCodec<SewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, SewingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<SewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SewingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int count = buffer.readVarInt();
            if (count < 1 || count > 9) {
                throw new IllegalArgumentException("Invalid sewing recipe ingredient count: " + count);
            }

            List<Ingredient> ingredients = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }
            return new SewingRecipe(ingredients, ItemStack.STREAM_CODEC.decode(buffer));
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SewingRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}
