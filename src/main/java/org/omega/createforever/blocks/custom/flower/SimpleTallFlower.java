package org.omega.createforever.blocks.custom.flower;


import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.omega.createforever.blocks.ModBlocks;

public class SimpleTallFlower extends TallFlowerBlock {

    private final Item dye;

    public SimpleTallFlower (MapColor mapColor, Item dye) {
        super(Properties.of()
                .mapColor(mapColor)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
        this.dye = dye;
    }

    public Item getDye () {
        return dye;
    }
}
