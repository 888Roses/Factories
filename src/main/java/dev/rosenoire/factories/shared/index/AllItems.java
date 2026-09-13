package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.BiFunction;
import java.util.function.Function;

@ApiStatus.NonExtendable
public interface AllItems {
    BlockItem REDSTONE_RADIO = blockItem(AllBlocks.REDSTONE_RADIO);

    static void initialize() {
    }

    private static @NonNull BlockItem blockItem(@NonNull Block block) {
        return blockItem(
                block.builtInRegistryHolder()
                        .key()
                        .identifier()
                        .getPath(),
                BlockItem::new,
                block,
                new Properties()
        );
    }

    private static <BI extends BlockItem> @NonNull BI blockItem(
            @NonNull String path,
            @NonNull BiFunction<Block, Properties, BI> factory,
            @NonNull Block block,
            @NonNull Properties properties
    ) {
        return item(
                path,
                p -> factory.apply(block, p.useBlockDescriptionPrefix()),
                properties
        );
    }

    private static <I extends Item> @NonNull I item(@NonNull String path,
                                                    @NonNull Function<Properties, I> factory,
                                                    @NonNull Properties properties) {
        var identifier = Factories.id(path);
        var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        properties.setId(resourceKey);

        var item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }
}
