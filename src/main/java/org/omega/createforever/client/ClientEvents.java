package org.omega.createforever.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.omega.createforever.CreateForever;
import org.omega.createforever.items.custom.BaseHatItem;

@EventBusSubscriber(modid = CreateForever.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerHatModels (ModelEvent.RegisterAdditional event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BaseHatItem hat) {
                event.register(ModelResourceLocation.standalone(hat.getHatModelLocation()));
            }
        }
    }

    @SubscribeEvent
    public static void onAddLayers (EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skinType : event.getSkins()) {
            var renderer = event.getSkin(skinType);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                livingRenderer.addLayer(new CustomHatLayer(livingRenderer));
            }
        }

        var armorStandRenderer = event.getRenderer(net.minecraft.world.entity.EntityType.ARMOR_STAND);
        if (armorStandRenderer instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new CustomHatLayer(livingRenderer));
        }
    }
}