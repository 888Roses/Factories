package dev.rosenoire.factories.data.generators;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

public class FactoriesCommonModelProvider extends FabricModelProvider {
    public FactoriesCommonModelProvider(@NonNull FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockModelGenerators) {
        FactoriesBlockModelProvider.generateBlockModels(blockModelGenerators);
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {
        FactoriesItemModelProvider.generateItemModels(itemModelGenerators);
    }
}
