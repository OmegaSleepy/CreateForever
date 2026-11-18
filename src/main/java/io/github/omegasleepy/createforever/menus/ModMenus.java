package io.github.omegasleepy.createforever.menus;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.omegasleepy.createforever.CreateForever.MODID;

public final class ModMenus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);

    public static final net.neoforged.neoforge.registries.DeferredHolder<MenuType<?>, MenuType<SewingTableMenu>>
            SEWING_MACHINE = MENUS.register("sewing_machine",
            () -> IMenuTypeExtension.create(SewingTableMenu::new));

    private ModMenus() {
    }

    public static void init(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }
}
