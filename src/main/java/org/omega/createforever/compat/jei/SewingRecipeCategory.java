package org.omega.createforever.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.recipe.SewingRecipe;

import java.util.List;

public final class SewingRecipeCategory
        implements IRecipeCategory<RecipeHolder<SewingRecipe>> {

    private static final int WIDTH = 174;
    private static final int HEIGHT = 76;

    private static final int OUTER_BORDER   = 0xFF4A3B2C;
    private static final int PARCHMENT_BG   = 0xFFEDE3CE;
    private static final int INNER_SHADE    = 0xFFE2D4B7;
    private static final int STITCH_THREAD  = 0xFF8C4A32;
    private static final int CHALK_LINE     = 0xFFC7B89D;

    private final IDrawable icon;

    public SewingRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.SEWING_TABLE.get())
        );
    }

    @Override
    @NotNull
    public RecipeType<RecipeHolder<SewingRecipe>> getRecipeType() {
        return SewingJeiPlugin.SEWING;
    }

    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("recipe.category.createforever.sewing");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull RecipeHolder<SewingRecipe> recipe,
            @NotNull IFocusGroup focuses
    ) {
        var rawIngredients = recipe.value().getIngredients();

        List<List<ItemStack>> groupedStacks = new java.util.ArrayList<>();

        for (var ingredient : rawIngredients) {
            List<ItemStack> currentStacks = List.of(ingredient.getItems());
            boolean merged = false;

            for (List<ItemStack> existingGroup : groupedStacks) {
                if (areIngredientStacksEqual(existingGroup, currentStacks)) {
                    for (ItemStack stack : existingGroup) {
                        stack.grow(1);
                    }
                    merged = true;
                    break;
                }
            }

            if (!merged) {
                List<ItemStack> copies = currentStacks.stream()
                        .map(ItemStack::copy)
                        .toList();
                groupedStacks.add(copies);
            }
        }

        for (int i = 0; i < groupedStacks.size(); i++) {
            int x = 14 + (i % 3) * 20;
            int y = 9 + (i / 3) * 20;

            builder.addInputSlot(x, y)
                    .setStandardSlotBackground()
                    .addItemStacks(groupedStacks.get(i));
        }

        builder.addOutputSlot(140, 29)
                .setOutputSlotBackground()
                .addItemStack(recipe.value().getResult());
    }

    private boolean areIngredientStacksEqual(List<ItemStack> a, List<ItemStack> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!ItemStack.isSameItemSameComponents(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void draw(
            @NotNull RecipeHolder<SewingRecipe> recipe,
            @NotNull IRecipeSlotsView recipeSlotsView,
            @NotNull GuiGraphics graphics,
            double mouseX,
            double mouseY
    ) {
        //base
        graphics.fill(0, 0, WIDTH, HEIGHT, OUTER_BORDER);
        graphics.fill(1, 1, WIDTH - 1, HEIGHT - 1, PARCHMENT_BG);
        graphics.fill(5, 5, WIDTH - 5, HEIGHT - 5, INNER_SHADE);

        //perimeter stitch
        drawDashedHLine(graphics, 4, WIDTH - 4, 4, 3, 2, STITCH_THREAD);
        drawDashedHLine(graphics, 4, WIDTH - 4, HEIGHT - 5, 3, 2, STITCH_THREAD);
        drawDashedVLine(graphics, 4, 4, HEIGHT - 4, 3, 2, STITCH_THREAD);
        drawDashedVLine(graphics, WIDTH - 5, 4, HEIGHT - 4, 3, 2, STITCH_THREAD);

        //corner stitches
        drawCrossStitch(graphics, 7, 7, STITCH_THREAD);
        drawCrossStitch(graphics, WIDTH - 10, 7, STITCH_THREAD);
        drawCrossStitch(graphics, 7, HEIGHT - 10, STITCH_THREAD);
        drawCrossStitch(graphics, WIDTH - 10, HEIGHT - 10, STITCH_THREAD);

        //chalk grid outline
        for (int col = 0; col <= 3; col++) {
            int gx = 12 + col * 19;
            drawDashedVLine(graphics, gx, 7, 64, 1, 2, CHALK_LINE);
        }
        for (int row = 0; row <= 3; row++) {
            int gy = 7 + row * 19;
            drawDashedHLine(graphics, 12, 69, gy, 1, 2, CHALK_LINE);
        }
        //frame
        int outX = 137;
        int outY = 26;
        int outSize = 22;
        graphics.fill(outX, outY, outX + outSize, outY + outSize, PARCHMENT_BG);
        drawCornerTicks(graphics, outX - 2, outY - 2, outSize + 4, outSize + 4, STITCH_THREAD);

        drawStitchedArrow(graphics, 81, 37, 48, STITCH_THREAD);
    }

    private void drawDashedHLine(GuiGraphics g, int x1, int x2, int y, int dash, int gap, int color) {
        for (int x = x1; x < x2; x += dash + gap) {
            g.fill(x, y, Math.min(x + dash, x2), y + 1, color);
        }
    }

    private void drawDashedVLine(GuiGraphics g, int x, int y1, int y2, int dash, int gap, int color) {
        for (int y = y1; y < y2; y += dash + gap) {
            g.fill(x, y, x + 1, Math.min(y + dash, y2), color);
        }
    }

    private void drawStitchedArrow(GuiGraphics g, int startX, int centerY, int length, int color) {
        g.fill(startX, centerY - 1, startX + 2, centerY + 2, color);

        int endX = startX + length;
        drawDashedHLine(g, startX + 4, endX - 4, centerY, 4, 2, color);

        for (int i = 0; i < 4; i++) {
            g.fill(endX - i, centerY - i, endX - i + 1, centerY - i + 1, color);
            g.fill(endX - i, centerY + i, endX - i + 1, centerY + i + 1, color);
        }
    }

    private void drawCornerTicks(GuiGraphics g, int x, int y, int w, int h, int color) {
        int len = 3;
        // top left
        g.fill(x, y, x + len, y + 1, color);
        g.fill(x, y, x + 1, y + len, color);
        // top right
        g.fill(x + w - len, y, x + w, y + 1, color);
        g.fill(x + w - 1, y, x + w, y + len, color);
        // bottom left
        g.fill(x, y + h - 1, x + len, y + h, color);
        g.fill(x, y + h - len, x + 1, y + h, color);
        // bottom right
        g.fill(x + w - len, y + h - 1, x + w, y + h, color);
        g.fill(x + w - 1, y + h - len, x + w, y + h, color);
    }

    private void drawCrossStitch(GuiGraphics g, int x, int y, int color) {
        g.fill(x, y, x + 1, y + 1, color);
        g.fill(x + 2, y, x + 3, y + 1, color);
        g.fill(x + 1, y + 1, x + 2, y + 2, color);
        g.fill(x, y + 2, x + 1, y + 3, color);
        g.fill(x + 2, y + 2, x + 3, y + 3, color);
    }
}