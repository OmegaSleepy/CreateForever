package org.omega.createforever.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.omega.createforever.util.ModDataComponents;

import java.util.List;

public class PropelStaff extends Item {

    private static final int MAX_ENDERS = 160;

    public PropelStaff() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    private int getEnders(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ENDER_COUNT.get(), 0);
    }

    private void setEnders(ItemStack stack, int value) {
        stack.set(ModDataComponents.ENDER_COUNT.get(), value);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        int enders = getEnders(stack);

        if (enders < 0 || !player.isCreative()) {
            return InteractionResultHolder.fail(stack);
        }

        if (!player.isCreative()) setEnders(stack, enders - 1);

        if (player.isShiftKeyDown()) {
            player.push(0, 1.25, 0);

        } else {
            Vec3 look = player.getLookAngle();
            player.push(look.scale(1.8f));
            player.push(0, 0.3, 0);
        }

        player.swing(hand);
        BlockPos blockPos = player.blockPosition();
        level.playSound(player, blockPos, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1, 1);

        player.getCooldowns().addCooldown(this, 1);

        if (!level.isClientSide) {
            player.displayClientMessage(
                    Component.literal("Pearls: " + (enders - 1)), true);

            var x = blockPos.getX() + 0.5;
            var y = blockPos.getY();
            var z = blockPos.getZ() + 0.5;

            ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, x, y, z, 50, 0, 0, 0, 1);
        }


        return InteractionResultHolder.success(stack);
    }


    @Override
    public boolean overrideOtherStackedOnMe(
            @NotNull ItemStack staffStack,
            @NotNull ItemStack carriedStack,
            @NotNull Slot slot,
            @NotNull ClickAction action,
            @NotNull Player player,
            @NotNull SlotAccess access
    ) {
        if (action != ClickAction.SECONDARY)
            return false;

        if (!carriedStack.is(Items.ENDER_PEARL))
            return false;

        int current = getEnders(staffStack);
        if (current >= MAX_ENDERS)
            return false;

        int toAdd = Math.min(MAX_ENDERS - current, carriedStack.getCount());

        setEnders(staffStack, current + toAdd);
        carriedStack.shrink(toAdd);

        player.displayClientMessage(
                Component.literal("Pearls: " + (current + toAdd)), true);

        return true;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(13f * getEnders(stack) / MAX_ENDERS);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float stackMaxDamage = MAX_ENDERS;
        float f = Math.max(0.0F, (stackMaxDamage - (float) getEnders(stack) / stackMaxDamage));
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext tooltipContext, List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
        tooltips.add(Component.literal("Pearls: " + getEnders(stack) + "/" + MAX_ENDERS));
    }
}
