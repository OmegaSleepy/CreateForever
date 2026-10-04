package org.omega.createforever.items.custom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HatItem extends BaseHatItem {
    private static final List<BaseHatItem> hats = new ArrayList<>();

    public HatItem () {
        super(ArmorMaterials.LEATHER, new Item.Properties());
        hats.add(this);
    }

    public static List<BaseHatItem> getHats () {
        return hats;
    }

    public @NotNull ResourceLocation getHatModelLocation () {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(this);
        return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), "item/" + itemId.getPath());
    }
}
