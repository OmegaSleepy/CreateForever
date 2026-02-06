package org.omega.createforever.items.custom;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Card extends Item {
    static List<Card> cards = new ArrayList<>();
    public String suit;
    public String value;

    public Card(String suit, String value) {
        super(new Item.Properties().stacksTo(1));
        this.suit = suit;
        this.value = value;
        cards.add(this);
    }
}
