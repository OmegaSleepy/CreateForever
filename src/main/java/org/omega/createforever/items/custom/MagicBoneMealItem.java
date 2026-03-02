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

        if(!world.isClientSide()) {
            assert context.getPlayer() != null;

            if(true){
                context.getPlayer().giveExperiencePoints(1);
                context.getItemInHand().shrink(1);

                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);

                for (int i = 0; i < 30; i++) {
                    world.addFreshEntity(
                            new ItemEntity(
                                    world,
                                    x+0.5,y,z+0.5,
                                    blockClicked.asItem().getDefaultInstance()
                            )
                    );
                }

                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }


        return super.useOn(context);
    }

//
//                for (int i = 0; i < 2; i++) {
//                    world.spawnEntity(
//                            new ItemEntity(
//                                    world,
//                                    x+0.5,y,z+0.5,
//                                    blockClicked.getPickStack(world,context.getBlockPos(),blockClicked.getDefaultState())));
//                }
//                for (int i = 0; i < 300; i++) {((ServerWorld) world).spawnParticles(
//                        ModParticles.GREEN_SPARKLE,
//                        x + 0.5,
//                        y + 2,
//                        z + 0.5,
//                        1, 0, 1, 0, 0.01
//                );}
//
//            } else{
//                return ActionResult.FAIL;
//            }
//
//        }
//
//        return ActionResult.SUCCESS;
//    }


    private boolean isValidBlock (Block blockClicked) {
        return blockClicked.getStateDefinition().any().is(ModTags.Blocks.FLOWERS) ||
                blockClicked.getStateDefinition().any().is(BlockTags.FLOWERS) ||
                blockClicked.getStateDefinition().any().is(BlockTags.TALL_FLOWERS);
    }
}
