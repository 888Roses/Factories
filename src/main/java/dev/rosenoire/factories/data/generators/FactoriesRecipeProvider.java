package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.index.AllItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class FactoriesRecipeProvider extends FabricRecipeProvider {
    public FactoriesRecipeProvider(@NonNull FabricPackOutput output,
                                   @NonNull CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries,
                                                           @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.REDSTONE, AllItems.REDSTONE_RADIO)
                        .pattern("TQP")
                        .pattern("SSS")
                        .define('T', Items.REDSTONE_TORCH)
                        .define('Q', ConventionalItemTags.QUARTZ_GEMS)
                        .define('P', ConventionalItemTags.ENDER_PEARLS)
                        .define('S', ConventionalItemTags.STONES)
                        .group("multi_bench")
                        .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                        .save(output);
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "Factories Recipe Provider";
    }
}
