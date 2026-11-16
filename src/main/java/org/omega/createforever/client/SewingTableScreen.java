package org.omega.createforever.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.omega.createforever.CreateForever;
import org.omega.createforever.menus.SewingTableMenu;
import org.omega.createforever.recipe.SewingRecipe;
import org.omega.createforever.util.ModTags;

import java.util.*;

public class SewingTableScreen extends AbstractContainerScreen<SewingTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "textures/gui/sewing_machine.png");

    private static final int LIST_X = 8;
    private static final int LIST_WIDTH = 136;
    private static final int PREVIEW_X = 151;
    private static final int PREVIEW_WIDTH = 82;
    private static final int RECIPE_X = 239;
    private static final int RECIPE_WIDTH = 123;
    private static final int ROW_HEIGHT = 18;

    private static final int LIST_TOP = 51;
    private static final int LIST_BOTTOM = 212;

    private static final int LABEL_COLOR = 0x404040;
    private static final int LIGHT_TEXT_COLOR = 0xFFFFFF;
    private static final int MUTED_TEXT_COLOR = 0xC6C6C6;

    private static final int MISSING_TEXT_COLOR = 0xFF5555;
    private static final int AVAILABLE_TEXT_COLOR = 0x55FF55;

    private final Map<TagKey<Item>, Boolean> expandedTags = new HashMap<>();
    private final Map<String, Component> ingredientNameCache = new HashMap<>();
    private final Set<Item> craftableHatSet;
    private final TagGroup rootGroup;
    private final RemotePlayer previewPlayer;
    private List<ListRow> visibleRows = List.of();
    private int scrollOffset;

    private EditBox searchBox;
    private ItemStack hoveredTooltipStack = ItemStack.EMPTY;

    public SewingTableScreen (SewingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 370;
        imageHeight = 220;
        titleLabelY = 8;

        craftableHatSet = new HashSet<>(menu.getCraftableHats());
        rootGroup = createTagTree();
        expandTags(rootGroup);

        ClientLevel level = Minecraft.getInstance().level;
        previewPlayer = new RemotePlayer(level, Minecraft.getInstance().player.getGameProfile()) {
            @Override
            public boolean shouldShowName() {
                return false;
            }

            @Override
            public Component getName() {
                return Component.empty();
            }

            @Override
            public Component getDisplayName() {
                return Component.empty();
            }
        };
        previewPlayer.setCustomNameVisible(false);
        previewPlayer.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
    }

    @Override
    protected void init() {
        super.init();

        searchBox = new EditBox(font, leftPos + LIST_X, topPos + 27, LIST_WIDTH, 18,
                Component.translatable("screen.createforever.sewing_machine.search"));
        searchBox.setMaxLength(50);
        searchBox.setBordered(true);
        searchBox.setVisible(true);
        searchBox.setTextColor(LIGHT_TEXT_COLOR);
        searchBox.setHint(Component.translatable("screen.createforever.sewing_machine.search"));
        searchBox.setResponder(text -> {
            scrollOffset = 0;
            rebuildRows();
        });

        this.addRenderableWidget(searchBox);
        rebuildRows();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        hoveredTooltipStack = ItemStack.EMPTY;
        super.render(graphics, mouseX, mouseY, partialTick);
        if (!hoveredTooltipStack.isEmpty()) {
            graphics.renderTooltip(font, hoveredTooltipStack, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 512, 512);

        renderHatList(graphics, mouseX, mouseY);
        renderPreview(graphics, mouseX, mouseY);
        renderRecipe(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        // Dark gray (0x404040) without shadow for crisp readability on #c6c6c6 panels
        graphics.drawString(font, title, 8, titleLabelY, LABEL_COLOR, false);

        graphics.drawString(font, Component.translatable("screen.createforever.sewing_machine.ingredients"),
                RECIPE_X, titleLabelY, LABEL_COLOR, false);
    }

    private void renderHatList(GuiGraphics graphics, int mouseX, int mouseY) {
        int top = topPos + LIST_TOP;
        int bottom = topPos + LIST_BOTTOM;
        boolean hasScrollbar = visibleRows.size() * ROW_HEIGHT > bottom - top;
        int contentWidth = hasScrollbar ? LIST_WIDTH - 6 : LIST_WIDTH;

        graphics.enableScissor(leftPos + LIST_X, top, leftPos + LIST_X + contentWidth, bottom);

        for (int rowIndex = scrollOffset; rowIndex < visibleRows.size(); rowIndex++) {
            int y = top + (rowIndex - scrollOffset) * ROW_HEIGHT;
            if (y >= bottom) {
                break;
            }

            ListRow row = visibleRows.get(rowIndex);
            int x = leftPos + LIST_X + 3 + row.depth * 10;
            boolean hovered = mouseX >= leftPos + LIST_X && mouseX < leftPos + LIST_X + contentWidth
                    && mouseY >= Math.max(y, top) && mouseY < Math.min(y + ROW_HEIGHT, bottom);

            if (row.item != null && menu.getSelectedHat() == row.item) {
                graphics.fill(leftPos + LIST_X, y, leftPos + LIST_X + contentWidth, y + ROW_HEIGHT, 0xFF7A7A7A);
                graphics.renderOutline(leftPos + LIST_X, y, contentWidth, ROW_HEIGHT, 0xFFFFFFFF);
            } else if (hovered) {
                graphics.fill(leftPos + LIST_X, y, leftPos + LIST_X + contentWidth, y + ROW_HEIGHT, 0xFF686868);
            }

            if (row.item != null) {
                ItemStack stack = new ItemStack(row.item);
                graphics.renderItem(stack, x, y + 1);
                if (hovered) {
                    hoveredTooltipStack = stack;
                }
                int maxTextWidth = contentWidth - (x - (leftPos + LIST_X)) - 22;
                graphics.drawString(font, fitText(row.name, maxTextWidth),
                        x + 20, y + 5, LIGHT_TEXT_COLOR, true);
            } else {
                // Subtle category header strip
                graphics.fill(leftPos + LIST_X, y, leftPos + LIST_X + contentWidth, y + ROW_HEIGHT, 0x44000000);
                boolean expanded = expandedTags.getOrDefault(row.tag, false);
                graphics.drawString(font, expanded ? "▾" : "▸", x, y + 5, MUTED_TEXT_COLOR, true);
                int maxTextWidth = contentWidth - (x - (leftPos + LIST_X)) - 14;
                graphics.drawString(font, fitText(row.name, maxTextWidth),
                        x + 10, y + 5, LIGHT_TEXT_COLOR, true);
            }
        }
        graphics.disableScissor();

        if (hasScrollbar) {
            int barX = leftPos + LIST_X + LIST_WIDTH - 5;
            graphics.fill(barX, top, barX + 5, bottom, 0xFF000000);
            int thumbHeight = Math.max(14, (bottom - top) * (bottom - top) / (visibleRows.size() * ROW_HEIGHT));
            int maxScroll = maxScrollRows();
            int thumbY = top + (bottom - top - thumbHeight) * scrollOffset / Math.max(1, maxScroll);
            graphics.fill(barX, thumbY, barX + 5, thumbY + thumbHeight, 0xFFC6C6C6);
            graphics.fill(barX + 4, thumbY, barX + 5, thumbY + thumbHeight, 0xFF555555);
            graphics.fill(barX, thumbY, barX + 4, thumbY + 1, 0xFFFFFFFF);
        }
    }

    private void renderPreview(GuiGraphics graphics, int mouseX, int mouseY) {
        Item selectedHat = menu.getSelectedHat();
        previewPlayer.setItemSlot(EquipmentSlot.HEAD,
                selectedHat == null ? ItemStack.EMPTY : new ItemStack(selectedHat));

        int boxLeft = leftPos + PREVIEW_X + 2;
        int boxTop = topPos + 28;
        int boxRight = leftPos + PREVIEW_X + PREVIEW_WIDTH - 2;
        int boxBottom = topPos + 210;

        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
                boxLeft, boxTop,
                boxRight, boxBottom,
                56, 0.0625F,
                mouseX, mouseY,
                previewPlayer);
    }

    private void renderRecipe(GuiGraphics graphics, int mouseX, int mouseY) {
        SewingRecipe recipe = menu.getSelectedRecipe();
        if (recipe == null) {
            graphics.drawCenteredString(font,
                    Component.translatable("screen.createforever.sewing_machine.no_recipe"),
                    leftPos + RECIPE_X + RECIPE_WIDTH / 2, topPos + 98, MUTED_TEXT_COLOR);
            return;
        }

        int y = topPos + 31;
        for (IngredientLine line : ingredientLines(recipe)) {
            int rowLeft = leftPos + RECIPE_X + 2;
            boolean hovered = mouseX >= rowLeft && mouseX < leftPos + RECIPE_X + RECIPE_WIDTH - 2
                    && mouseY >= y && mouseY < y + 18;

            if (hovered) {
                graphics.fill(rowLeft, y, leftPos + RECIPE_X + RECIPE_WIDTH - 2, y + 18, 0x33FFFFFF);
                hoveredTooltipStack = line.displayStack;
            }

            graphics.renderItem(line.displayStack, rowLeft + 1, y + 1);
            Component label = line.count > 1
                    ? Component.literal(line.name.getString() + " x" + line.count)
                    : line.name;
            boolean missing = menu.countMatchingItems(line.ingredient) < line.count;
            graphics.drawString(font, fitText(label, RECIPE_WIDTH - 26),
                    rowLeft + 21, y + 5, missing ? MISSING_TEXT_COLOR : AVAILABLE_TEXT_COLOR, true);

            y += 20;
            if (y > topPos + 165) {
                graphics.drawString(font, "...", rowLeft + 4, y - 2, MUTED_TEXT_COLOR, true);
                break;
            }
        }

        // Render Sew button (enabled green or disabled dark gray)
        int buttonX = leftPos + RECIPE_X - 1;
        int buttonY = topPos + 190;
        int buttonW = RECIPE_WIDTH + 2;
        int buttonH = 22;
        boolean canCraft = menu.canCraftSelected();
        boolean hovered = canCraft && mouseX >= buttonX && mouseX < buttonX + buttonW
                && mouseY >= buttonY && mouseY < buttonY + buttonH;

        int bgColor = !canCraft ? 0xFF555555 : (hovered ? 0xFF6B8050 : 0xFF53683B);
        graphics.fill(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, bgColor);
        // Beveled button border
        graphics.fill(buttonX, buttonY, buttonX + buttonW, buttonY + 1, canCraft ? 0x99FFFFFF : 0x44FFFFFF);
        graphics.fill(buttonX, buttonY, buttonX + 1, buttonY + buttonH, canCraft ? 0x99FFFFFF : 0x44FFFFFF);
        graphics.fill(buttonX, buttonY + buttonH - 1, buttonX + buttonW, buttonY + buttonH, 0xFF000000);
        graphics.fill(buttonX + buttonW - 1, buttonY, buttonX + buttonW, buttonY + buttonH, 0xFF000000);

        graphics.drawCenteredString(font,
                Component.translatable("screen.createforever.sewing_machine.sew"),
                buttonX + buttonW / 2, buttonY + 7, canCraft ? LIGHT_TEXT_COLOR : MUTED_TEXT_COLOR);
    }

    private void rebuildRows() {
        List<ListRow> rows = new ArrayList<>();
        String filter = searchBox != null ? searchBox.getValue().toLowerCase().trim() : "";

        if (!filter.isEmpty()) {
            craftableHatSet.stream()
                    .filter(item -> new ItemStack(item).getHoverName().getString().toLowerCase().contains(filter))
                    .sorted(Comparator.comparing(item -> new ItemStack(item).getHoverName().getString()))
                    .forEach(item -> rows.add(ListRow.item(item, 0)));
            visibleRows = rows;
            return;
        }

        Set<Item> displayedItems = new HashSet<>();
        for (TagGroup child : rootGroup.children) {
            rows.add(ListRow.tag(child.tag, 0));
            if (expandedTags.getOrDefault(child.tag, true)) {
                appendGroupRows(rows, child, 0, displayedItems);
            }
        }
        visibleRows = rows;
    }

    private List<IngredientLine> ingredientLines(SewingRecipe recipe) {
        Map<String, IngredientLine> lines = new LinkedHashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ItemStack[] choices = ingredient.getItems();
            if (choices.length == 0) {
                continue;
            }

            String key = ingredientKey(choices);
            IngredientLine existing = lines.get(key);
            if (existing == null) {
                lines.put(key, new IngredientLine(ingredient, choices[0], ingredientName(choices, key), 1));
            } else {
                existing.count++;
            }
        }
        return List.copyOf(lines.values());
    }

    private String ingredientKey(ItemStack[] choices) {
        return String.join("|", Arrays.stream(choices)
                .map(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()) + ":" + stack.getComponents().hashCode())
                .sorted()
                .toList());
    }

    private Component ingredientName(ItemStack[] choices, String key) {
        if (choices.length == 1) {
            return choices[0].getHoverName();
        }
        return ingredientNameCache.computeIfAbsent(key, ignored -> resolveTagIngredientName(choices));
    }

    private Component resolveTagIngredientName(ItemStack[] choices) {
        Set<Item> choiceItems = Arrays.stream(choices)
                .map(ItemStack::getItem)
                .collect(java.util.stream.Collectors.toSet());
        for (TagKey<Item> tag : BuiltInRegistries.ITEM.getTagNames().toList()) {
            Set<Item> tagItems = BuiltInRegistries.ITEM.getTag(tag)
                    .stream()
                    .flatMap(holders -> holders.stream().map(Holder::value))
                    .collect(java.util.stream.Collectors.toSet());
            if (tagItems.equals(choiceItems)) {
                return Component.translatable("tag." + tag.location().getNamespace() + "." + tag.location().getPath());
            }
        }
        return Component.translatable("screen.createforever.sewing_machine.tag_ingredient");
    }

    private TagGroup createTagTree() {
        TagKey<Item> rootTag = ModTags.Items.CRAFTABLE_HATS;
        Map<TagKey<Item>, Set<Item>> tagContents = new HashMap<>();
        for (TagKey<Item> tag : BuiltInRegistries.ITEM.getTagNames().toList()) {
            if (tag.equals(rootTag)) {
                continue;
            }
            Set<Item> contents = BuiltInRegistries.ITEM.getTag(tag)
                    .stream()
                    .flatMap(holders -> holders.stream().map(Holder::value))
                    .collect(java.util.stream.Collectors.toSet());
            if (!contents.isEmpty() && craftableHatSet.containsAll(contents)
                    && !contents.equals(craftableHatSet)) {
                tagContents.put(tag, contents);
            }
        }

        TagGroup root = new TagGroup(rootTag, craftableHatSet);
        Map<TagKey<Item>, TagGroup> groups = new HashMap<>();
        groups.put(rootTag, root);
        tagContents.forEach((tag, contents) -> groups.put(tag, new TagGroup(tag, contents)));

        for (Map.Entry<TagKey<Item>, Set<Item>> entry : tagContents.entrySet()) {
            TagKey<Item> parentTag = rootTag;
            int parentSize = craftableHatSet.size();
            for (Map.Entry<TagKey<Item>, Set<Item>> candidate : tagContents.entrySet()) {
                if (!candidate.getKey().equals(entry.getKey())
                        && candidate.getValue().size() > entry.getValue().size()
                        && candidate.getValue().containsAll(entry.getValue())
                        && candidate.getValue().size() < parentSize) {
                    parentTag = candidate.getKey();
                    parentSize = candidate.getValue().size();
                }
            }
            groups.get(parentTag).children.add(groups.get(entry.getKey()));
        }
        sortChildren(root);
        return root;
    }

    private void expandTags(TagGroup group) {
        expandedTags.put(group.tag, true);
        for (TagGroup child : group.children) {
            expandTags(child);
        }
    }

    private void sortChildren(TagGroup group) {
        group.children.sort(Comparator.comparing(child -> child.tag.location().toString()));
        for (TagGroup child : group.children) {
            sortChildren(child);
        }
    }

    private void appendGroupRows(List<ListRow> rows, TagGroup group, int depth, Set<Item> displayedItems) {
        for (TagGroup child : group.children) {
            rows.add(ListRow.tag(child.tag, depth + 1));
            if (expandedTags.getOrDefault(child.tag, true)) {
                appendGroupRows(rows, child, depth + 1, displayedItems);
            }
        }

        group.items.stream()
                .filter(item -> !displayedItems.contains(item))
                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                .forEach(item -> {
                    rows.add(ListRow.item(item, depth + 1));
                    displayedItems.add(item);
                });
    }

    private int maxScrollRows() {
        int rowsInView = (LIST_BOTTOM - LIST_TOP) / ROW_HEIGHT;
        return Math.max(0, visibleRows.size() - rowsInView);
    }

    private Component fitText(Component component, int maxWidth) {
        String text = component.getString();
        if (font.width(text) <= maxWidth) {
            return component;
        }
        while (!text.isEmpty() && font.width(text + "...") > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }
        return Component.literal(text + "...");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isInside(mouseX, mouseY, LIST_X, LIST_TOP, LIST_WIDTH, LIST_BOTTOM - LIST_TOP)) {
            int rowIndex = scrollOffset + (int) (mouseY - topPos - LIST_TOP) / ROW_HEIGHT;
            if (rowIndex >= 0 && rowIndex < visibleRows.size()) {
                ListRow row = visibleRows.get(rowIndex);
                if (row.item != null) {
                    int index = menu.getCraftableHats().indexOf(row.item);
                    menu.setSelectedHatIndex(index);
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, index);
                    minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0F));
                    return true;
                } else if (searchBox.getValue().isEmpty()) {
                    boolean expanding = !expandedTags.getOrDefault(row.tag, false);
                    expandedTags.put(row.tag, expanding);
                    rebuildRows();
                    scrollOffset = Math.min(scrollOffset, maxScrollRows());
                    minecraft.getSoundManager().play(SimpleSoundInstance.forUI(
                            SoundEvents.UI_BUTTON_CLICK, expanding ? 1.1F : 0.9F));
                    return true;
                }
            }
        }

        if (button == 0 && menu.canCraftSelected()
                && isInside(mouseX, mouseY, RECIPE_X - 1, 190, RECIPE_WIDTH + 2, 22)) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, SewingTableMenu.CRAFT_BUTTON);
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_TAKE_RESULT, 1.0F));
            minecraft.player.closeContainer();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }
            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
            if (keyCode == 256) { // GLFW.GLFW_KEY_ESCAPE
                this.searchBox.setFocused(false);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isInside(mouseX, mouseY, LIST_X, LIST_TOP, LIST_WIDTH, LIST_BOTTOM - LIST_TOP)) {
            scrollOffset = Math.max(0, Math.min(maxScrollRows(), scrollOffset - (int) Math.signum(scrollY) * 3));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private boolean isInside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= leftPos + x && mouseX < leftPos + x + width
                && mouseY >= topPos + y && mouseY < topPos + y + height;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private record ListRow(TagKey<Item> tag, Item item, Component name, int depth) {
        private static ListRow tag(TagKey<Item> tag, int depth) {
            return new ListRow(tag, null,
                    Component.translatable("tag." + tag.location().getNamespace() + "." + tag.location().getPath()),
                    depth);
        }

        private static ListRow item(Item item, int depth) {
            return new ListRow(null, item, new ItemStack(item).getHoverName(), depth);
        }
    }

    private static final class TagGroup {
        private final TagKey<Item> tag;
        private final Set<Item> items;
        private final List<TagGroup> children = new ArrayList<>();

        private TagGroup(TagKey<Item> tag, Set<Item> items) {
            this.tag = tag;
            this.items = items;
        }
    }

    private static final class IngredientLine {
        private final Ingredient ingredient;
        private final ItemStack displayStack;
        private final Component name;
        private int count;

        private IngredientLine(Ingredient ingredient, ItemStack displayStack, Component name, int count) {
            this.ingredient = ingredient;
            this.displayStack = displayStack;
            this.name = name;
            this.count = count;
        }
    }
}