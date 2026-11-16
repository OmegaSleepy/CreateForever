package org.omega.createforever.menus;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.recipe.ModRecipeTypes;
import org.omega.createforever.recipe.SewingRecipe;
import org.omega.createforever.util.ModTags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SewingTableMenu extends AbstractContainerMenu {
    public static final int CRAFT_BUTTON = 32767;

    private final Inventory inventory;
    private final BlockPos blockPos;
    private final ContainerData data = new SimpleContainerData(1);
    private final List<Item> craftableHats;

    public SewingTableMenu (int containerId, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, inventory, buffer.readBlockPos());
    }

    public SewingTableMenu (int containerId, Inventory inventory, BlockPos blockPos) {
        super(ModMenus.SEWING_MACHINE.get(), containerId);
        this.inventory = inventory;
        this.blockPos = blockPos;
        this.craftableHats = BuiltInRegistries.ITEM.getTag(ModTags.Items.CRAFTABLE_HATS)
                .stream()
                .flatMap(tag -> tag.stream().map(Holder::value))
                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                .toList();
        this.data.set(0, craftableHats.isEmpty() ? -1 : 0);
        addDataSlots(data);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == CRAFT_BUTTON) {
            if (!player.level().isClientSide) {
                craftSelected(player);
            }
            return true;
        }
        if (buttonId >= 0 && buttonId < craftableHats.size()) {
            setSelectedHatIndex(buttonId);
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(inventory.player.level(), blockPos),
                player, ModBlocks.SEWING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    public List<Item> getCraftableHats() {
        return craftableHats;
    }

    public int getSelectedHatIndex() {
        return data.get(0);
    }

    public void setSelectedHatIndex(int index) {
        if (index >= 0 && index < craftableHats.size()) {
            data.set(0, index);
        }
    }

    public Item getSelectedHat() {
        int index = getSelectedHatIndex();
        return index >= 0 && index < craftableHats.size() ? craftableHats.get(index) : null;
    }

    public SewingRecipe getSelectedRecipe() {
        Item selectedHat = getSelectedHat();
        if (selectedHat == null) {
            return null;
        }

        Level level = inventory.player.level();
        List<RecipeHolder<SewingRecipe>> recipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.SEWING.get());
        SewingRecipe fallback = null;
        for (RecipeHolder<SewingRecipe> holder : recipes) {
            SewingRecipe recipe = holder.value();
            if (recipe.getResultItem(level.registryAccess()).is(selectedHat)) {
                if (fallback == null) {
                    fallback = recipe;
                }
                if (findIngredientSlots(recipe) != null) {
                    return recipe;
                }
            }
        }
        return fallback;
    }

    public boolean canCraftSelected() {
        SewingRecipe recipe = getSelectedRecipe();
        return recipe != null && findIngredientSlots(recipe) != null;
    }

    private void craftSelected(Player player) {
        SewingRecipe recipe = getSelectedRecipe();
        if (recipe == null) {
            return;
        }

        List<Integer> ingredientSlots = findIngredientSlots(recipe);
        if (ingredientSlots == null) {
            return;
        }

        for (int slot : ingredientSlots) {
            inventory.removeItem(slot, 1);
        }

        ItemStack result = recipe.getResultItem(player.level().registryAccess()).copy();
        if (!inventory.add(result)) {
            player.drop(result, false);
        }

        inventory.setChanged();
        this.broadcastChanges();

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.sendAllDataToRemote();
        }
    }

    public int countMatchingItems(Ingredient ingredient) {
        int total = 0;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private List<Integer> findIngredientSlots(SewingRecipe recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();
        int[] remaining = new int[inventory.getContainerSize()];
        for (int slot = 0; slot < remaining.length; slot++) {
            remaining[slot] = inventory.getItem(slot).getCount();
        }

        int[] assigned = new int[ingredients.size()];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            order.add(i);
        }
        order.sort(Comparator.comparingInt(index -> matchingStackCount(ingredients.get(index), remaining)));

        if (!assignIngredients(ingredients, order, 0, remaining, assigned)) {
            return null;
        }

        List<Integer> slots = new ArrayList<>(assigned.length);
        for (int slot : assigned) {
            slots.add(slot);
        }
        return slots;
    }

    private int matchingStackCount(Ingredient ingredient, int[] remaining) {
        int matches = 0;
        for (int slot = 0; slot < remaining.length; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (remaining[slot] > 0 && ingredient.test(stack)) {
                matches++;
            }
        }
        return matches;
    }

    private boolean assignIngredients(List<Ingredient> ingredients, List<Integer> order, int index,
                                      int[] remaining, int[] assigned) {
        if (index == order.size()) {
            return true;
        }

        int ingredientIndex = order.get(index);
        Ingredient ingredient = ingredients.get(ingredientIndex);
        for (int slot = 0; slot < remaining.length; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (remaining[slot] > 0 && ingredient.test(stack)) {
                remaining[slot]--;
                assigned[ingredientIndex] = slot;
                if (assignIngredients(ingredients, order, index + 1, remaining, assigned)) {
                    return true;
                }
                remaining[slot]++;
            }
        }
        return false;
    }
}
