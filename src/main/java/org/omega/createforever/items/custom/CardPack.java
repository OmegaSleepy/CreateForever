package org.omega.createforever.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CardPack extends Item {
    public CardPack() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        var x = player.getX();
        var y = player.getY();
        var z = player.getZ();

        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();

            for (Card card : Card.cards) {
                ItemEntity entity = new ItemEntity(level, x, y, z, new ItemStack(card));

                double strength = 0.5;
                entity.setDeltaMovement(look.scale(strength));

                level.addFreshEntity(entity);
            }
        }


        BlockPos pos = new BlockPos((int)x, (int)y, (int)z);

        player.swing(interactionHand);
        player.getInventory().getItem(interactionHand.ordinal()+1).setCount(0);
        level.playSound(player, pos, SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F);

        return super.use(level, player, interactionHand);
    }
}
