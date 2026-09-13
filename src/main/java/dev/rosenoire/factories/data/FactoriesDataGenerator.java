package dev.rosenoire.factories.data;

import dev.rosenoire.factories.data.generators.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

public class FactoriesDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(FactoriesLanguage::new);
        pack.addProvider(FactoriesCommonModelProvider::new);
        pack.addProvider(FactoriesSoundProvider::new);
        pack.addProvider(FactoriesLootTableProvider::new);
        pack.addProvider(FactoriesRecipeProvider::new);
    }
}
