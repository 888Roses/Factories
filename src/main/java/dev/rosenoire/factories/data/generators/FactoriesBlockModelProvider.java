package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.content.redstone_radio.RedstoneRadioBlock;
import dev.rosenoire.factories.shared.index.AllBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.client.data.models.model.ModelLocationUtils.getModelLocation;

@ApiStatus.NonExtendable
public interface FactoriesBlockModelProvider {
    private static @NonNull MultiVariant plain(@NonNull Block block,
                                               @NonNull String suffix) {
        return plainVariant(getModelLocation(block, suffix));
    }

    private static @NonNull MultiVariant plain(@NonNull Block block) {
        return plainVariant(getModelLocation(block));
    }

    static void generateBlockModels(@NonNull BlockModelGenerators generators) {
        extractRedstoneRadio(generators);
    }

    private static void extractRedstoneRadio(@NonNull BlockModelGenerators generators) {
        var on = plain(AllBlocks.REDSTONE_RADIO, "/on");
        var off = plain(AllBlocks.REDSTONE_RADIO, "/off");

        var channel_on = plain(AllBlocks.REDSTONE_RADIO, "/channel_on");
        var channel_off = plain(AllBlocks.REDSTONE_RADIO, "/channel_off");

        generators.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(AllBlocks.REDSTONE_RADIO)
                .with(PropertyDispatch
                        .initial(RedstoneRadioBlock.POWERED, RedstoneRadioBlock.HAS_CHANNEL)
                        .select(true, false, on)
                        .select(false, false, off)
                        .select(true, true, channel_on)
                        .select(false, true, channel_off)
                )
                .with(PropertyDispatch
                        .modify(BlockStateProperties.ORIENTATION)
                        .generate(BlockModelGenerators::applyRotation)
                )
        );
    }
}
