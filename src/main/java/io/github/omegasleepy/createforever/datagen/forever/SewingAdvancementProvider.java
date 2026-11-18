package io.github.omegasleepy.createforever.datagen.forever;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import io.github.omegasleepy.createforever.CreateForever;
import io.github.omegasleepy.createforever.advancements.SewHatTrigger;
import io.github.omegasleepy.createforever.blocks.ModBlocks;
import io.github.omegasleepy.createforever.items.ModItems;
import io.github.omegasleepy.createforever.util.HatTagContents;
import io.github.omegasleepy.createforever.util.ModTags;
import io.github.omegasleepy.createforever.util.SewingAdvancements;

import java.util.List;
import java.util.function.Consumer;

public class SewingAdvancementProvider implements AdvancementGenerator {
    private static final ResourceLocation SPOOL_ADVANCEMENT = id("sewing_spool");
    private static final ResourceLocation STATION_ADVANCEMENT = id("sewing_station");

    private static final ResourceLocation BACKGROUND_TEXTURE = id("textures/gui/advancements/backgrounds/sewing_advancements.png");

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output,
                         ExistingFileHelper existingFileHelper) {

        AdvancementHolder spool = Advancement.Builder.advancement()
                .display(ModItems.SEWING_SPOOL, title("sewing_spool"), description("sewing_spool"),
                        BACKGROUND_TEXTURE, AdvancementType.TASK, true, true, false)
                .addCriterion("crafted_spool", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("sewing_spool")))
                .save(output, SPOOL_ADVANCEMENT.toString());

        AdvancementHolder station = Advancement.Builder.advancement()
                .parent(spool)
                .display(ModBlocks.SEWING_TABLE, title("sewing_station"), description("sewing_station"),
                        null, AdvancementType.TASK, true, true, false)
                .addCriterion("crafted_station", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("sewing_table")))
                .save(output, STATION_ADVANCEMENT.toString());

        for (var group : HatTagContents.GROUPS.entrySet()) {
            Item icon = getIconForTag(group.getKey(), group.getValue());
            addSewAllAdvancement(output, station, group.getKey(), group.getValue(), icon);
        }

        addSewAllAdvancement(output, station, ModTags.Items.CRAFTABLE_HATS, HatTagContents.CRAFTABLE_HATS, ModItems.MAFIA);
    }

    private static Item getIconForTag(TagKey<Item> tag, List<Item> items) {
        if (tag.equals(ModTags.Items.CASUAL_HATS)) {
            return ModItems.CAPELA.get();
        }
        if (tag.equals(ModTags.Items.CHRISTMAS_HATS)) {
            return ModItems.ELF_HAT.get();
        }
        if (tag.equals(ModTags.Items.CHRISTMAS_HATS_DYEABLE)) {
            return ModItems.RED_CHRISTMAS_HAT.get();
        }
        if (tag.equals(ModTags.Items.HALLOWEEN_HATS)) {
            return ModItems.MASK_P.get();
        }
        if (tag.equals(ModTags.Items.LEGACY_HATS)) {
            return ModItems.CHEF.get();
        }

        return items.isEmpty() ? ModItems.SEWING_SPOOL.asItem() : items.getFirst();
    }

    private static void addSewAllAdvancement(Consumer<AdvancementHolder> output, AdvancementHolder parent,
                                             TagKey<Item> tag, List<Item> items, ItemLike icon) {
        String path = tag.location().getPath();
        boolean challenge = tag.equals(ModTags.Items.CRAFTABLE_HATS);
        Advancement.Builder builder = Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, title("hats." + path), description("hats." + path),
                        null, challenge ? AdvancementType.CHALLENGE : AdvancementType.GOAL,
                        true, true, false);

        for (Item item : items) {
            builder.addCriterion(SewingAdvancements.criterionName(item),
                    SewHatTrigger.sewn(item));
        }
        builder.save(output, id("hats/" + path).toString());
    }

    private static Component title(String key) {
        return Component.translatable("advancements.createforever." + key + ".title");
    }

    private static Component description(String key) {
        return Component.translatable("advancements.createforever." + key + ".description");
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, path);
    }
}