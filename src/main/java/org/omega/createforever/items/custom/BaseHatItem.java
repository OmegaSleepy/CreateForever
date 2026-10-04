package org.omega.createforever.items.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.omega.createforever.client.BaseHatClientExtensions;

import java.util.function.Consumer;

public abstract class BaseHatItem extends ArmorItem {
    protected BaseHatItem (Holder<ArmorMaterial> material, Item.Properties properties) {
        super(material, Type.HELMET, properties);
    }

    /**
     * Returns this hat's standalone model resource. By default, a hat registered as
     * {@code example:top_hat} uses {@code assets/example/models/item/top_hat.json}.
     */
    public @NotNull ResourceLocation getHatModelLocation () {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(this);
        return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), "item" + itemId.getPath());
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient (Consumer<IClientItemExtensions> consumer) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            consumer.accept(BaseHatClientExtensions.INSTANCE);
        }
    }
}
