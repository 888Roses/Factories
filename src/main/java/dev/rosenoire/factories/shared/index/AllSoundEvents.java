package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

@ApiStatus.NonExtendable
public interface AllSoundEvents {
    SoundEvent REDSTONE_RADIO_CHANGE_CHANNEL = blockSound(AllBlocks.REDSTONE_RADIO, "change_channel");

    static void initialize() {
    }

    private static @NonNull SoundEvent blockSound(@NonNull Block block, @NonNull String path) {
        return sound(block.getDescriptionId() + "." + path);
    }

    private static @NonNull SoundEvent sound(@NonNull String path) {
        return SoundEvent.createVariableRangeEvent(Factories.id(path));
    }
}
