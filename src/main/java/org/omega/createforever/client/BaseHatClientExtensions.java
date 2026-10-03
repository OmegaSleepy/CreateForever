package org.omega.createforever.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public final class BaseHatClientExtensions implements IClientItemExtensions {
    public static final BaseHatClientExtensions INSTANCE = new BaseHatClientExtensions();

    private static final Model EMPTY_ARMOR_MODEL = new Model(RenderType::entitySolid) {
        @Override
        public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer,
                                  int packedLight, int packedOverlay, int color) {
        }
    };

    private BaseHatClientExtensions() {
    }

    @Override
    public @NotNull Model getGenericArmorModel(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack,
                                               @NotNull EquipmentSlot equipmentSlot,
                                               @NotNull HumanoidModel<?> original) {
        return EMPTY_ARMOR_MODEL;
    }
}
