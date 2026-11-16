package org.omega.createforever.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.omega.createforever.menus.SewingTableMenu;

public class SewingTableBlock extends Block {

    public SewingTableBlock (Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem (BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            level.playLocalSound(pos, SoundEvents.UI_LOOM_SELECT_PATTERN, SoundSource.BLOCKS, 1.0f, 1.0f, true);
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) -> new SewingTableMenu(containerId, inventory, pos),
                    Component.translatable("block.createforever.sewing_table")
            ), buffer -> buffer.writeBlockPos(pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
