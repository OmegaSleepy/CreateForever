package org.omega.createforever.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public final class SewHatTrigger extends SimpleCriterionTrigger<SewHatTrigger.Instance> {
    Logger LOGGER = LogManager.getLogger();
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, Item hat) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(hat);

        LOGGER.info("Sewing advancement triggered for: {}", itemId);

        trigger(player, instance -> instance.item().equals(itemId));
    }

    public static Criterion<Instance> sewn(Item hat) {
        return ModCriteriaTriggers.SEW_HAT.get().createCriterion(
                new Instance(Optional.empty(), BuiltInRegistries.ITEM.getKey(hat)));
    }

    public record Instance(Optional<ContextAwarePredicate> player, ResourceLocation item)
            implements SimpleInstance {
        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                ResourceLocation.CODEC.fieldOf("item").forGetter(Instance::item)
        ).apply(instance, Instance::new));
    }
}
