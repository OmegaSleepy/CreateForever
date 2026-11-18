package io.github.omegasleepy.createforever.compat.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import io.github.omegasleepy.createforever.blocks.ModBlocks;
import io.github.omegasleepy.createforever.recipe.ModRecipeTypes;
import io.github.omegasleepy.createforever.recipe.SewingRecipe;

import static io.github.omegasleepy.createforever.CreateForever.MODID;

@JeiPlugin
public final class SewingJeiPlugin implements IModPlugin {
    public static final RecipeType<RecipeHolder<SewingRecipe>> SEWING =
            RecipeType.createRecipeHolderType(ResourceLocation.fromNamespaceAndPath(MODID, "sewing"));

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SewingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(SEWING, level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SEWING.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.SEWING_TABLE.get(), SEWING);
    }
}
