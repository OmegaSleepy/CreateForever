package org.omega.createforever.blocks.custom.flower;


import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class SimpleFlower extends FlowerBlock {

    private final Item dye;

    public SimpleFlower (int effectDuration, MapColor mapColor, Item dye) {
        super(MobEffects.CONFUSION, effectDuration, Properties.of()
                .mapColor(mapColor)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .offsetType(OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY));
        this.dye = dye;
    }

    public Item getDye () {
        return dye;
    }
}
