package dev.rosenoire.factories.client.index;

import dev.rosenoire.factories.client.content.debugging.entries.DummyScreenEntry;
import dev.rosenoire.factories.shared.Factories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface AllDebugEntries {
    Identifier ITEM_TAGS = create("item_tags", new DummyScreenEntry());

    static void register() {
    }

    private static @NonNull Identifier create(@NonNull String name, @NonNull DebugScreenEntry entry) {
        return DebugScreenEntries.register(Factories.id(name), entry);
    }

    static @NonNull DebugScreenEntryStatus status(@NonNull Identifier location) {
        return Minecraft.getInstance().debugEntries.getStatus(location);
    }

    static boolean isVisible(@NonNull Identifier location) {
        return Minecraft.getInstance().debugEntries.isCurrentlyEnabled(location);
    }
}
