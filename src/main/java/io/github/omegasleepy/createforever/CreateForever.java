package io.github.omegasleepy.createforever;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import io.github.omegasleepy.createforever.blocks.ModBlocks;
import io.github.omegasleepy.createforever.advancements.ModCriteriaTriggers;
import io.github.omegasleepy.createforever.creative_tab.ModTabs;
import io.github.omegasleepy.createforever.items.ModItems;
import io.github.omegasleepy.createforever.menus.ModMenus;
import io.github.omegasleepy.createforever.recipe.ModRecipeTypes;
import io.github.omegasleepy.createforever.sound.ModSounds;
import org.slf4j.Logger;

@Mod(CreateForever.MODID)
public class CreateForever {
    public static final String MODID = "createforever";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateForever (IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModItems.init(modEventBus);
        ModCriteriaTriggers.init(modEventBus);
        ModBlocks.init(modEventBus);
        ModTabs.init(modEventBus);
        ModMenus.init(modEventBus);
        ModRecipeTypes.init(modEventBus);
        ModSounds.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup (final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting (ServerStartingEvent event) {
        LOGGER.info(MODID + " is starting");
    }
}