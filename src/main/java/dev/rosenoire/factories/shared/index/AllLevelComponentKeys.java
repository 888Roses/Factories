package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.Factories;
import dev.rosenoire.factories.shared.content.redstone_radio.RadioNetwork;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.ComponentV3;

public interface AllLevelComponentKeys {
    ComponentKey<RadioNetwork> RADIO_NETWORK = key("radio_network", RadioNetwork.class);

    private static <T extends ComponentV3> @NonNull ComponentKey<T> key(@NonNull String path,
                                                                        @NonNull Class<T> clazz) {
        return ComponentRegistry.getOrCreate(Factories.id(path), clazz);
    }
}
