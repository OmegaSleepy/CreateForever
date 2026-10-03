package org.omega.createforever.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.items.custom.Card;
import org.omega.createforever.items.custom.CardPack;
import org.omega.createforever.items.custom.HatItem;

import java.util.List;

import static org.omega.createforever.CreateForever.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final List<String> SUITS = List.of("Clubs", "Diamonds", "Hearts", "Spades");
    public static final List<String> RANKS = List.of("Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King");

    static {
        for (String suit : SUITS) {
            for (String value : RANKS) {
                ITEMS.register((suit.toLowerCase() + "_" + value.toLowerCase()), Card::new);
            }
        }
    }

    public static final DeferredItem<Item> CARD_PACK = ITEMS.register("card_pack", CardPack::new);

    public static final DeferredItem<Item> CAPELA = ITEMS.register("capela", HatItem::new);
    public static final DeferredItem<Item> COOK = ITEMS.register("cook", HatItem::new);
    public static final DeferredItem<Item> CROWN = ITEMS.register("crown", HatItem::new);
    public static final DeferredItem<Item> MAFIA = ITEMS.register("mafia", HatItem::new);
    public static final DeferredItem<Item> MASK_P = ITEMS.register("mask_pumpkin", HatItem::new);
    public static final DeferredItem<Item> MASK_S = ITEMS.register("mask_slime", HatItem::new);
    public static final DeferredItem<Item> CHEF = ITEMS.register("chef", HatItem::new);
    public static final DeferredItem<Item> WITCH = ITEMS.register("witch", HatItem::new);


    public static void init (IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}
