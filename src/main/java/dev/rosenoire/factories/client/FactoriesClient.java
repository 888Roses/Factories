package dev.rosenoire.factories.client;

import dev.rosenoire.factories.client.index.AllBlockEntityRenderers;
import dev.rosenoire.factories.client.index.AllCreativeTabs;
import net.fabricmc.api.ClientModInitializer;

public class FactoriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AllCreativeTabs.initialize();
        AllBlockEntityRenderers.initialize();
    }
}
