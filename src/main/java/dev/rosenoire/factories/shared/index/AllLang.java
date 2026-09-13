package dev.rosenoire.factories.shared.index;

import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

public interface AllLang {
    String REDSTONE_RADIO_CHANGE_CHANNEL = subtitle(AllSoundEvents.REDSTONE_RADIO_CHANGE_CHANNEL);

    private static @NonNull String subtitle(@NonNull SoundEvent soundEvent) {
        return soundEvent.location().toLanguageKey("subtitle");
    }
}
