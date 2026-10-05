package org.omega.createforever.events;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.omega.createforever.CreateForever;

import java.util.List;

@EventBusSubscriber(modid = CreateForever.MODID)
public class PlayerHeadDropHandler {

    @SubscribeEvent
    public static void onPlayerDeath (LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (victim.level().isClientSide()) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player killer)) {
            return;
        }

        if (killer == victim) {
            return;
        }

        GameProfile originalProfile = victim.getGameProfile();

        GameProfile staticProfile = new GameProfile(
                originalProfile.getId(),
                originalProfile.getName()
        );

        originalProfile.getProperties().get("textures").forEach(
                property -> staticProfile.getProperties().put("textures", property)
        );

        ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);

        headStack.set(
                DataComponents.PROFILE,
                new ResolvableProfile(staticProfile)
        );

        Component customName = Component.literal(
                victim.getScoreboardName() + "'s Head"
        );

        headStack.set(DataComponents.CUSTOM_NAME, customName);

        Component loreText = Component.literal(
                victim.getScoreboardName()
                        + " was slain by "
                        + killer.getScoreboardName()
        );

        headStack.set(
                DataComponents.LORE,
                new ItemLore(List.of(loreText))
        );

        ItemEntity itemEntity = new ItemEntity(
                victim.level(),
                victim.getX(),
                victim.getY(),
                victim.getZ(),
                headStack
        );

        victim.level().addFreshEntity(itemEntity);
    }
}