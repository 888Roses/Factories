package dev.rosenoire.factories.client.index;

import dev.rosenoire.factories.client.content.redstone_radio.RedstoneRadioBlockEntityRenderer;
import dev.rosenoire.factories.shared.index.AllBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public interface AllBlockEntityRenderers {
    static void initialize() {
        BlockEntityRenderers.register(AllBlockEntityTypes.REDSTONE_RADIO, RedstoneRadioBlockEntityRenderer::new);
    }
}
