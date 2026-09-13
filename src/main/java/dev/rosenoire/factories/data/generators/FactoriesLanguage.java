package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.index.AllBlocks;
import dev.rosenoire.factories.shared.index.AllLang;
import dev.rosenoire.factories.shared.index.AllSoundEvents;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class FactoriesLanguage extends FabricLanguageProvider {
    public FactoriesLanguage(@NonNull FabricPackOutput packOutput,
                             @NonNull CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup,
                                     @NonNull TranslationBuilder translationBuilder) {
        translationBuilder.add(AllBlocks.REDSTONE_RADIO, "Redstone Radio");

        translationBuilder.add(
                AllLang.REDSTONE_RADIO_CHANGE_CHANNEL,
                "Changed redstone radio channel"
        );
    }
}
