package org.omega.createforever.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.omega.createforever.CreateForever;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, CreateForever.MODID);

    public static final Supplier<SoundEvent> SEWING = SOUND_EVENTS.register("sewing",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CreateForever.MODID, "sewing")));

    public static void init(IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}