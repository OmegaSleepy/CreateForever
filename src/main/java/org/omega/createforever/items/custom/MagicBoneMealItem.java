package org.omega.createforever.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.UnknownNullability;
import org.omega.createforever.util.ModTags;

public class MagicBoneMealItem extends Item {

    public MagicBoneMealItem() {
        super(new Item.Properties().stacksTo(65));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block blockClicked = world.getBlockState(pos).getBlock();

        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();


        if(!isValidBlock(world.getBlockState(pos))) return InteractionResult.FAIL;

        if(!world.isClientSide()) {
            assert context.getPlayer() != null;

            if(true){
                context.getPlayer().giveExperiencePoints(1);
                context.getItemInHand().shrink(1);

                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);

                for (int i = 0; i < 4; i++) {
                    world.addFreshEntity(
                            new ItemEntity(
                                    world,
                                    x+0.5,y,z+0.5,
                                    blockClicked.asItem().getDefaultInstance()
                            )
                    );
                }

                return InteractionResult.CONSUME;
            }
            return InteractionResult.FAIL;
        }


        return super.useOn(context);
    }


    private boolean isValidBlock (@UnknownNullability BlockState blockClicked) {

        return blockClicked.is(ModTags.Blocks.FLOWERS) ||
                blockClicked.is(BlockTags.FLOWERS) ||
                blockClicked.is(BlockTags.TALL_FLOWERS);
    }
}
