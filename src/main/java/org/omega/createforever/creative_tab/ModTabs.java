package org.omega.createforever.creative_tab;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.items.ModItems;
import org.omega.createforever.items.custom.Card;

import static org.omega.createforever.CreateForever.MODID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final Holder<CreativeModeTab> cards = TABS.register("tab.createforever.cards",
            () -> CreativeModeTab.builder()
            .title(Component.translatable("tab.createforever.cards"))
            .icon(() -> new ItemStack(Card.cards.get(0).asItem()))
            .displayItems(((parameters, output) -> {
                output.accept(ModItems.CARD_PACK);
                for (Card card : Card.cards) {
                    output.accept(card);
                }
            }))
            .build());

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.PROPULTION_STAFF);
        }
        if(event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
            event.accept(ModBlocks.CONCRETE);
            event.accept(ModBlocks.CONCRETE_POWDER);
        }
    }

    public static void init(IEventBus bus) {
        TABS.register(bus);
        bus.addListener(ModTabs::buildContents);
    }

}
