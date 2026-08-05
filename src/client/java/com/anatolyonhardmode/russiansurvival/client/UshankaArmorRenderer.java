package com.anatolyonhardmode.russiansurvival.client;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class UshankaArmorRenderer {
    private static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "ushanka"), "main");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            RussianSurvival.MOD_ID, "textures/models/armor/ushanka_3d.png");
    private static ModelPart hat;

    public static void initialize() {
        EntityModelLayerRegistry.registerModelLayer(LAYER, UshankaArmorRenderer::createLayer);
        ArmorRenderer.register(UshankaArmorRenderer::render, ModItems.USHANKA);
    }

    public static void ensureBaked() {
        if (hat == null) hat = Minecraft.getInstance().getEntityModels().bakeLayer(LAYER).getChild("hat");
    }

    private static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hat", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -9.2F, -5.0F, 10.0F, 6.0F, 10.0F)
                        .texOffs(40, 0).addBox(-4.0F, -7.5F, -5.8F, 8.0F, 3.0F, 1.0F)
                        .texOffs(0, 17).addBox(-5.7F, -6.8F, -4.0F, 2.0F, 7.0F, 8.0F)
                        .texOffs(20, 17).addBox(3.7F, -6.8F, -4.0F, 2.0F, 7.0F, 8.0F)
                        .texOffs(42, 8).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack,
                               LivingEntity entity, EquipmentSlot slot, int light,
                               HumanoidModel<LivingEntity> contextModel) {
        if (slot != EquipmentSlot.HEAD) return;
        ensureBaked();
        matrices.pushPose();
        contextModel.head.translateAndRotate(matrices);
        VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers,
                RenderType.entityCutoutNoCull(TEXTURE), stack.hasFoil());
        hat.render(matrices, consumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        matrices.popPose();
    }

    private UshankaArmorRenderer() {}
}
