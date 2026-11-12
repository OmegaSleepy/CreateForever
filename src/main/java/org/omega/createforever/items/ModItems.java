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
    public static final DeferredItem<Item> CAT_EARS = ITEMS.register("cat_ears", HatItem::new);
    public static final DeferredItem<Item> BARET = ITEMS.register("baret", HatItem::new);

    public static final DeferredItem<Item> WHITE_CHRISTMAS_HAT = ITEMS.register("christmas_white_hat", HatItem::new);
    public static final DeferredItem<Item> LIGHT_GRAY_CHRISTMAS_HAT = ITEMS.register("christmas_light_gray_hat", HatItem::new);
    public static final DeferredItem<Item> GRAY_CHRISTMAS_HAT = ITEMS.register("christmas_gray_hat", HatItem::new);
    public static final DeferredItem<Item> BLACK_CHRISTMAS_HAT = ITEMS.register("christmas_black_hat", HatItem::new);
    public static final DeferredItem<Item> BROWN_CHRISTMAS_HAT = ITEMS.register("christmas_brown_hat", HatItem::new);
    public static final DeferredItem<Item> RED_CHRISTMAS_HAT = ITEMS.register("christmas_red_hat", HatItem::new);
    public static final DeferredItem<Item> ORANGE_CHRISTMAS_HAT = ITEMS.register("christmas_orange_hat", HatItem::new);
    public static final DeferredItem<Item> YELLOW_CHRISTMAS_HAT = ITEMS.register("christmas_yellow_hat", HatItem::new);
    public static final DeferredItem<Item> LIME_CHRISTMAS_HAT = ITEMS.register("christmas_lime_hat", HatItem::new);
    public static final DeferredItem<Item> GREEN_CHRISTMAS_HAT = ITEMS.register("christmas_green_hat", HatItem::new);
    public static final DeferredItem<Item> CYAN_CHRISTMAS_HAT = ITEMS.register("christmas_cyan_hat", HatItem::new);
    public static final DeferredItem<Item> LIGHT_BLUE_CHRISTMAS_HAT = ITEMS.register("christmas_light_blue_hat", HatItem::new);
    public static final DeferredItem<Item> BLUE_CHRISTMAS_HAT = ITEMS.register("christmas_blue_hat", HatItem::new);
    public static final DeferredItem<Item> PURPLE_CHRISTMAS_HAT = ITEMS.register("christmas_purple_hat", HatItem::new);
    public static final DeferredItem<Item> MAGENTA_CHRISTMAS_HAT = ITEMS.register("christmas_magenta_hat", HatItem::new);
    public static final DeferredItem<Item> PINK_CHRISTMAS_HAT = ITEMS.register("christmas_pink_hat", HatItem::new);

    public static void init (IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}
