package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.index.AllBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class FactoriesLootTableProvider extends FabricBlockLootSubProvider {
    public FactoriesLootTableProvider(@NonNull FabricPackOutput packOutput,
                                      @NonNull CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(AllBlocks.REDSTONE_RADIO);
    }
}
