package org.omega.createforever.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.omega.createforever.CreateForever;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CRAFTABLE_HATS = createTag("craftable_hats");

        public static final TagKey<Item> LEGACY_HATS = createTag("legacy_hats");
        public static final TagKey<Item> CHRISTMAS_HATS = createTag("christmas_hats");
        public static final TagKey<Item> HALLOWEEN_HATS = createTag("halloween_hats");
        public static final TagKey<Item> CASUAL_HATS = createTag("casual_hats");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, name));
        }
    }
}