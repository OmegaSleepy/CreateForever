package org.omega.createforever.advancements;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.CreateForever;

public final class ModCriteriaTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CreateForever.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, SewHatTrigger> SEW_HAT =
            TRIGGERS.register("sew_hat", SewHatTrigger::new);

    private ModCriteriaTriggers() {
    }

    public static void init(IEventBus modEventBus) {
        TRIGGERS.register(modEventBus);
    }
}
