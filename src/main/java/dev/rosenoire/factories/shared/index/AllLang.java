package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

public interface AllLang {
    String REDSTONE_RADIO_CHANGE_CHANNEL = subtitle(AllSoundEvents.REDSTONE_RADIO_CHANGE_CHANNEL);

    String DEBUG_TAGS_TITLE = debugging("tag_entries.title");
    String DEBUG_TAGS_ENTRY = debugging("tag_entries.entry");

    private static @NonNull String lang(@NonNull String prefix, @NonNull String key) {
        return prefix + "." + Factories.NAMESPACE + "." + key;
    }

    private static @NonNull String debugging(@NonNull String key) {
        return lang("debugging", key);
    }

    private static @NonNull String subtitle(@NonNull SoundEvent soundEvent) {
        return soundEvent.location().toLanguageKey("subtitle");
    }
}
