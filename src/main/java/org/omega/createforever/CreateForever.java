package org.omega.createforever;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.omega.createforever.blocks.ModBlocks;
import org.omega.createforever.creative_tab.ModTabs;
import org.omega.createforever.items.ModItems;
import org.omega.createforever.util.ModDataComponents;
import org.slf4j.Logger;

@Mod(CreateForever.MODID)
public class CreateForever {
    public static final String MODID = "createforever";
    private static final Logger LOGGER = LogUtils.getLogger();


    public CreateForever(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModItems.init(modEventBus);
        ModBlocks.init(modEventBus);
        ModTabs.init(modEventBus);
        ModDataComponents.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info(MODID + " is starting");
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("You are an interesting one");
        }
    }
}
