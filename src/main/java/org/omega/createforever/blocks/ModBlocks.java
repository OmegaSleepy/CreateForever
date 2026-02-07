package org.omega.createforever.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.items.ModItems;

import java.util.function.Supplier;

import static org.omega.createforever.CreateForever.MODID;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> CONCRETE =
            registerBlock("concrete",
                    () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE))
                    );

    public static final DeferredBlock<Block> CONCRETE_POWDER =
            registerBlock("concrete_powder",
                    () -> new ConcretePowderBlock(
                            CONCRETE.get(),
                            BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE_POWDER)) {
                    });


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
