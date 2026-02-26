package org.omega.createforever.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.blocks.custom.flower.SimpleFlower;
import org.omega.createforever.blocks.custom.flower.SimpleTallFlower;
import org.omega.createforever.items.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.omega.createforever.CreateForever.MODID;

public class ModBlocks {

    public static final List<DeferredBlock<? extends Block>> FLOWERS = new ArrayList<>();
    public static final List<DeferredBlock<? extends Block>> POTS = new ArrayList<>();

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

    public static final DeferredBlock<Block> ORCHID = registerFlower("orchid",
            () -> new SimpleFlower(100, MapColor.TERRACOTTA_PURPLE, Items.MAGENTA_DYE));

    public static final DeferredBlock<Block> CHRYSANTHEMUM = registerFlower("chrysanthemum",
            () -> new SimpleFlower(100, MapColor.COLOR_YELLOW, Items.YELLOW_DYE));

    public static final DeferredBlock<Block> AMARYLLIS = registerFlower("amaryllis",
            () -> new SimpleFlower(100, MapColor.COLOR_RED, Items.RED_DYE));

    public static final DeferredBlock<Block> VIOLET = registerFlower("violet",
            () -> new SimpleFlower(100, MapColor.COLOR_PURPLE, Items.PURPLE_DYE));

    public static final DeferredBlock<Block> BLUEBELL = registerFlower("bluebell",
            () -> new SimpleFlower(100, MapColor.TERRACOTTA_PURPLE, Items.BLUE_DYE));

    /* ===== TALL FLOWERS ===== */

//    public static final DeferredBlock<SimpleTallFlower> RED_GINGER = registerBlock("red_ginger",
//            () -> new SimpleTallFlower(MapColor.COLOR_RED, Items.RED_DYE));


    private static <T extends Block> DeferredBlock<T> registerFlower(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> flower = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, flower);

        DeferredBlock<FlowerPotBlock> pot = registerFlowerPot(name, flower);
        FLOWERS.add(flower);
        POTS.add(pot);
        return flower;
    }

    private static DeferredBlock<FlowerPotBlock> registerFlowerPot(String flowerName, DeferredBlock<? extends Block> flower) {
        return BLOCKS.register(
                "potted_" + flowerName,
                () -> new FlowerPotBlock(
                        flower.get(),  // safe here
                        BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ALLIUM)
                )
        );
    }

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
