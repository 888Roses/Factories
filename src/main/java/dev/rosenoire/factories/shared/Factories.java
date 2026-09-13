package dev.rosenoire.factories.shared;

import dev.rosenoire.factories.shared.index.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Factories implements ModInitializer {
    public static final String NAMESPACE = "factories";
    public static final Logger log = LoggerFactory.getLogger(NAMESPACE);

    public static @NonNull Identifier id(@NonNull String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }

    @Override
    public void onInitialize() {
        AllBlocks.initialize();
        AllBlockEntityTypes.initialize();
        AllItems.initialize();
        AllHooks.initialize();
        AllSoundEvents.initialize();
    }
}
