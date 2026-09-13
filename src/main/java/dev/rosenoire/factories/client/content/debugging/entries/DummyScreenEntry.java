package dev.rosenoire.factories.client.content.debugging.entries;

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DummyScreenEntry implements DebugScreenEntry {
    @Override
    public void display(@NonNull DebugScreenDisplayer displayer,
                        @Nullable Level serverOrClientLevel,
                        @Nullable LevelChunk clientChunk,
                        @Nullable LevelChunk serverChunk) {
    }
}
