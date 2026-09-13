package dev.rosenoire.factories.data.generators;

import dev.rosenoire.factories.shared.Factories;
import dev.rosenoire.factories.shared.index.AllLang;
import dev.rosenoire.factories.shared.index.AllSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class FactoriesSoundProvider extends FabricSoundsProvider {
    private SoundExporter exporter;

    public FactoriesSoundProvider(@NonNull PackOutput output,
                                  @NonNull CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NonNull Provider registryLookup,
                             @NonNull SoundExporter exporter) {
        this.exporter = exporter;

        this.sound(
                AllSoundEvents.REDSTONE_RADIO_CHANGE_CHANNEL,
                AllLang.REDSTONE_RADIO_CHANGE_CHANNEL,
                reference(SoundEvents.VILLAGER_WORK_CARTOGRAPHER)
        );
    }


    private void sound(@NonNull SoundEvent event,
                       @NonNull String subtitle,
                       SoundTypeBuilder.@NonNull RegistrationBuilder soundFile) {
        this.exporter.add(event, SoundTypeBuilder.of()
                .subtitle(subtitle)
                .sound(soundFile)
        );
    }

    private void sound(@NonNull SoundEvent event,
                       @NonNull String subtitle,
                       SoundTypeBuilder.@NonNull RegistrationBuilder soundFile,
                       int count) {
        this.exporter.add(event, SoundTypeBuilder.of()
                .subtitle(subtitle)
                .sound(soundFile, count)
        );
    }

    private void sound(@NonNull SoundEvent event,
                       @NonNull String subtitle,
                       @NonNull String soundFilePath) {
        this.exporter.add(event, SoundTypeBuilder.of()
                .subtitle(subtitle)
                .sound(file(soundFilePath))
        );
    }

    private void sound(@NonNull SoundEvent event,
                       @NonNull String subtitle,
                       @NonNull String soundFilePath,
                       int count) {
        this.exporter.add(event, SoundTypeBuilder.of()
                .subtitle(subtitle)
                .sound(file(soundFilePath), count)
        );
    }

    private SoundTypeBuilder.@NonNull RegistrationBuilder reference(@NonNull SoundEvent referenceOf) {
        return SoundTypeBuilder.RegistrationBuilder.ofEvent(referenceOf);
    }

    private SoundTypeBuilder.@NonNull RegistrationBuilder reference(@NonNull Holder<SoundEvent> referenceOf) {
        return SoundTypeBuilder.RegistrationBuilder.ofEvent(referenceOf);
    }

    private SoundTypeBuilder.@NonNull RegistrationBuilder file(@NonNull Identifier identifier) {
        return SoundTypeBuilder.RegistrationBuilder.ofFile(identifier);
    }

    private SoundTypeBuilder.@NonNull RegistrationBuilder file(@NonNull String localPath) {
        return file(Factories.id(localPath));
    }

    @Override
    public @NonNull String getName() {
        return "Factories Sound Provider";
    }
}
