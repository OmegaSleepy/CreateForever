package org.omega.createforever.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static org.omega.createforever.CreateForever.MODID;

public final class ModRecipeTypes {

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

    public static final Supplier<RecipeType<SewingRecipe>> SEWING =
            RECIPE_TYPES.register("sewing", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(MODID, "sewing")));
    public static final Supplier<RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER =
            RECIPE_SERIALIZERS.register("sewing", SewingRecipe.Serializer::new);

    private ModRecipeTypes() {
    }

    public static void init(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
