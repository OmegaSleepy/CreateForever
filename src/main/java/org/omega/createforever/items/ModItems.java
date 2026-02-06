package org.omega.createforever.items;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.items.custom.Card;
import org.omega.createforever.items.custom.CardPack;

import java.util.List;

import static org.omega.createforever.Createforever.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final List<String> SUITS = List.of("Clubs", "Diamonds", "Hearts", "Spades");
    public static final List<String> RANKS = List.of("Ace","2","3","4","5","6","7","8","9","10","Jack","Queen","King");

    static {
        for (String suit : SUITS) {
            for (String value : RANKS) {
                ITEMS.register((suit.toLowerCase()+"_"+value.toLowerCase()), () -> new Card(suit, value));
            }
        }
        ITEMS.register("card_pack", CardPack::new);
    }

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}
