package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.index.AllItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import static net.minecraft.client.data.models.model.ItemModelUtils.plainModel;
import static net.minecraft.client.data.models.model.ModelLocationUtils.getModelLocation;

@ApiStatus.NonExtendable
public interface FactoriesItemModelProvider {
    static void generateItemModels(@NonNull ItemModelGenerators generators) {
        var model = ModelTemplates.FLAT_ITEM.create(
                getModelLocation(AllItems.REDSTONE_RADIO),
                TextureMapping.layer0(AllItems.REDSTONE_RADIO),
                generators.modelOutput
        );

        generators.itemModelOutput.accept(AllItems.REDSTONE_RADIO, plainModel(model));
    }
}
