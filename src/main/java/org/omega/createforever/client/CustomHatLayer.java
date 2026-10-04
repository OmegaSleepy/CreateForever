package org.omega.createforever.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.omega.createforever.items.custom.BaseHatItem;

public class CustomHatLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public CustomHatLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                       @NotNull T livingEntity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack hatStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!(hatStack.getItem() instanceof BaseHatItem hat)) {
            return;
        }

        poseStack.pushPose();
        try {
            this.getParentModel().getHead().translateAndRotate(poseStack);

            poseStack.translate(0.0D, -0.25D, 0.0D);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(0.625F, -0.625F, -0.625F);

            BakedModel hatModel = Minecraft.getInstance().getModelManager().getModel(
                    ModelResourceLocation.standalone(hat.getHatModelLocation()));
            Minecraft.getInstance().getItemRenderer().render(
                hatStack,
                ItemDisplayContext.HEAD,
                false,
                poseStack,
                buffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                hatModel
            );
        } finally {
            poseStack.popPose();
        }
    }
}