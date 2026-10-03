package org.omega.createforever.items.custom;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.omega.createforever.client.BaseHatClientExtensions;

import java.util.function.Consumer;

public abstract class BaseHatItem extends ArmorItem {
    protected BaseHatItem(Holder<ArmorMaterial> material, Item.Properties properties) {
        super(material, Type.HELMET, properties);
    }

    /**
     * Returns this hat's standalone model resource. By default, a hat registered as
     * {@code example:top_hat} uses {@code assets/example/models/item/hat/top_hat.json}.
     */
    public @NotNull ResourceLocation getHatModelLocation() {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(this);
        return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), "item/hat/" + itemId.getPath());
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BaseHatClientExtensions.INSTANCE);
    }
}
