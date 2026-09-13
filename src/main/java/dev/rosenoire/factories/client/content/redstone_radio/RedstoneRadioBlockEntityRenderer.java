package dev.rosenoire.factories.client.content.redstone_radio;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.rosenoire.factories.shared.content.redstone_radio.RedstoneRadioBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RedstoneRadioBlockEntityRenderer implements BlockEntityRenderer<RedstoneRadioBlockEntity, RedstoneRadioBlockEntityRenderState> {
    public RedstoneRadioBlockEntityRenderer(BlockEntityRendererProvider.@NonNull Context ignoredContext) {
    }

    @Override
    public @NonNull RedstoneRadioBlockEntityRenderState createRenderState() {
        return new RedstoneRadioBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(@NonNull RedstoneRadioBlockEntity blockEntity,
                                   @NonNull RedstoneRadioBlockEntityRenderState state,
                                   float partialTicks,
                                   @NonNull Vec3 cameraPosition,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        var minecraft = Minecraft.getInstance();
        var hitResult = minecraft.hitResult;

        if (!(hitResult instanceof BlockHitResult bhr) || (!bhr.getBlockPos().equals(state.blockPos))) {
            return;
        }

        state.channelName = blockEntity.getChannel().orElse(null);
    }

    @Override
    public void submit(@NonNull RedstoneRadioBlockEntityRenderState state,
                       @NonNull PoseStack poseStack,
                       @NonNull SubmitNodeCollector submitNodeCollector,
                       @NonNull CameraRenderState camera) {
        if (state.channelName != null) {
            submitNodeCollector.submitNameTag(
                    poseStack,
                    new Vec3(0.5f, 0.5f, 0.5f),
                    0,
                    Component.literal(state.channelName),
                    true,
                    state.lightCoords,
                    state.blockPos.distToCenterSqr(camera.pos),
                    camera
            );
        }
    }
}
