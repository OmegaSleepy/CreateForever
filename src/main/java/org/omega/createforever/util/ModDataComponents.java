package org.omega.createforever.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static org.omega.createforever.CreateForever.MODID;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENDER_COUNT =
            COMPONENTS.register("ender_count",
                    () -> DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .build());

    public static void init(IEventBus modEventBus) {
        COMPONENTS.register(modEventBus);
    }

}
