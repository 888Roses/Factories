package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import dev.rosenoire.factories.shared.content.redstone_radio.RedstoneRadioBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@ApiStatus.NonExtendable
public interface AllBlocks {
    RedstoneRadioBlock REDSTONE_RADIO = block(
            "redstone_radio", RedstoneRadioBlock::new,
            Properties.ofFullCopy(Blocks.REPEATER)
                    .noOcclusion()
    );

    static void initialize() {
    }

    static <B extends Block> @NonNull B block(@NonNull String path,
                                              @NonNull Function<Properties, B> factory,
                                              @NonNull Properties properties) {
        var identifier = Factories.id(path);
        var resourceKey = ResourceKey.create(Registries.BLOCK, identifier);
        properties.setId(resourceKey);

        var block = factory.apply(properties);
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }
}
