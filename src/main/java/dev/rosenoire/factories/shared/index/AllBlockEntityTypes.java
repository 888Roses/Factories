package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import dev.rosenoire.factories.shared.content.redstone_radio.RedstoneRadioBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.UnaryOperator;

@ApiStatus.NonExtendable
public interface AllBlockEntityTypes {
    BlockEntityType<RedstoneRadioBlockEntity> REDSTONE_RADIO = blockEntity(
            "redstone_radio",
            RedstoneRadioBlockEntity::new,
            AllBlocks.REDSTONE_RADIO
    );

    static void initialize() {
    }

    private static <T extends BlockEntity> @NonNull BlockEntityType<T> blockEntity(
            @NonNull String path,
            FabricBlockEntityTypeBuilder.@NonNull Factory<T> factory,
            @NonNull Block... blocks
    ) {
        return blockEntity(path, factory, b -> b, blocks);
    }

    private static <T extends BlockEntity> @NonNull BlockEntityType<T> blockEntity(
            @NonNull String path,
            FabricBlockEntityTypeBuilder.@NonNull Factory<T> factory,
            @NonNull UnaryOperator<FabricBlockEntityTypeBuilder<T>> builder,
            @NonNull Block... blocks
    ) {
        var identifier = Factories.id(path);
        var resourceKey = ResourceKey.create(
                Registries.BLOCK_ENTITY_TYPE,
                identifier
        );
        var blockEntity = builder
                .apply(FabricBlockEntityTypeBuilder.create(factory, blocks))
                .build();
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, resourceKey, blockEntity);
    }
}
