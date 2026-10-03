package org.omega.createforever.items.custom;

import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HatItem extends BaseHatItem {
    public HatItem () {
        super(ArmorMaterials.LEATHER, new Item.Properties());
    }

    @Override
    public @NotNull ResourceLocation getHatModelLocation() {
        return ResourceLocation.fromNamespaceAndPath("createforever", "item/hat/mafia");
    }
}
