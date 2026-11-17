package org.omega.createforever.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.omega.createforever.items.ModItems;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class HatTagContents {
    public static final List<Item> CHRISTMAS_HATS_DYEABLE = List.of(
            ModItems.WHITE_CHRISTMAS_HAT.get(),
            ModItems.ORANGE_CHRISTMAS_HAT.get(),
            ModItems.MAGENTA_CHRISTMAS_HAT.get(),
            ModItems.LIGHT_BLUE_CHRISTMAS_HAT.get(),
            ModItems.YELLOW_CHRISTMAS_HAT.get(),
            ModItems.LIME_CHRISTMAS_HAT.get(),
            ModItems.PINK_CHRISTMAS_HAT.get(),
            ModItems.GRAY_CHRISTMAS_HAT.get(),
            ModItems.LIGHT_GRAY_CHRISTMAS_HAT.get(),
            ModItems.CYAN_CHRISTMAS_HAT.get(),
            ModItems.PURPLE_CHRISTMAS_HAT.get(),
            ModItems.BLUE_CHRISTMAS_HAT.get(),
            ModItems.BROWN_CHRISTMAS_HAT.get(),
            ModItems.GREEN_CHRISTMAS_HAT.get(),
            ModItems.RED_CHRISTMAS_HAT.get(),
            ModItems.BLACK_CHRISTMAS_HAT.get()
    );

    public static final List<Item> CHRISTMAS_HATS_OTHER = List.of(
            ModItems.DEER_ANTLERS.get(),
            ModItems.DEER_ANTLERS_RUDOLPH.get(),
            ModItems.ELF_HAT.get()
    );

    public static final Map<TagKey<Item>, List<Item>> GROUPS;
    public static final List<Item> CRAFTABLE_HATS;

    static {
        Map<TagKey<Item>, List<Item>> groups = new LinkedHashMap<>();
        groups.put(ModTags.Items.CHRISTMAS_HATS, Stream.concat(
                CHRISTMAS_HATS_OTHER.stream(), CHRISTMAS_HATS_DYEABLE.stream()).toList());
        groups.put(ModTags.Items.CHRISTMAS_HATS_DYEABLE, CHRISTMAS_HATS_DYEABLE);
        groups.put(ModTags.Items.LEGACY_HATS, List.of(
                ModItems.CAPELA.get(),
                ModItems.COOK.get(),
                ModItems.CROWN.get(),
                ModItems.MAFIA.get(),
                ModItems.MASK_P.get(),
                ModItems.MASK_S.get(),
                ModItems.CHEF.get(),
                ModItems.WITCH.get()
        ));
        groups.put(ModTags.Items.HALLOWEEN_HATS, List.of(
                ModItems.MASK_P.get(),
                ModItems.MASK_S.get(),
                ModItems.WITCH.get(),
                ModItems.CAT_EARS.get()
        ));
        groups.put(ModTags.Items.CASUAL_HATS, List.of(
                ModItems.CAPELA.get(),
                ModItems.BARET.get()
        ));
        GROUPS = Collections.unmodifiableMap(groups);
        CRAFTABLE_HATS = GROUPS.values().stream().flatMap(List::stream).distinct().toList();
    }

    private HatTagContents() {
    }
}
