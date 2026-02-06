package org.omega.createforever.items.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

        if(!level.isClientSide) {
            for (Card card : Card.cards) {
                level.addFreshEntity(new ItemEntity(level, x, y, z, new ItemStack(card)));
            }
        }

        player.swing(interactionHand);
        player.getInventory().getItem(interactionHand.ordinal()).setCount(0);

        return super.use(level, player, interactionHand);
    }
}
