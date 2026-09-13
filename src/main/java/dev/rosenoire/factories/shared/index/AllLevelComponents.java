package dev.rosenoire.factories.shared.index;

import dev.rosenoire.factories.shared.content.redstone_radio.RadioNetwork;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v8.level.LevelComponentFactoryRegistry;
import org.ladysnake.cca.api.v8.level.LevelComponentInitializer;

public class AllLevelComponents implements LevelComponentInitializer {
    @Override
    public void registerLevelComponentFactories(@NonNull LevelComponentFactoryRegistry registry) {
        registry.register(AllLevelComponentKeys.RADIO_NETWORK, RadioNetwork::new);
    }
}
