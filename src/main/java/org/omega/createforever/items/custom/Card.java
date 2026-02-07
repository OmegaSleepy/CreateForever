package org.omega.createforever.items.custom;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Card extends Item {
    public static List<Card> cards = new ArrayList<>();

    public Card() {
        super(new Item.Properties().stacksTo(1));
        cards.add(this);
    }
}
