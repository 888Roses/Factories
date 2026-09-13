package dev.rosenoire.factories.shared.index;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface AllBlockStateProperties {
    BooleanProperty HAS_CHANNEL = BooleanProperty.create("has_channel");
}
