package dev.rosenoire.factories.shared.content.redstone_radio;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rosenoire.factories.SharedConstants;
import dev.rosenoire.factories.shared.index.AllLevelComponentKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class RadioNetwork implements ComponentV3, AutoSyncedComponent, ServerTickingComponent {
    private static final Codec<List<Signal>> SIGNALS_CODEC = Signal.CODEC.listOf();
    private static final String SIGNALS_KEY = "signals";

    private final @NonNull Level level;
    private final @NonNull List<Signal> signals = new ArrayList<>();

    private int lazyTickCounter = 0;

    public RadioNetwork(@NonNull Level level) {
        this.level = level;
    }

    public @NonNull Level getLevel() {
        return this.level;
    }

    @Override
    public void readData(@NonNull ValueInput readView) {
        this.signals.clear();
        readView.read(SIGNALS_KEY, SIGNALS_CODEC).ifPresent(this.signals::addAll);
    }

    @Override
    public void writeData(@NonNull ValueOutput writeView) {
        writeView.store(SIGNALS_KEY, SIGNALS_CODEC, this.signals);
    }

    public void synchronise() {
        AllLevelComponentKeys.RADIO_NETWORK.sync(this.getLevel());
    }

    public void store(@NonNull BlockPos pos, @NonNull String name, int signal) {
        var positionNode = pos.asLong();
        this.signals.removeIf(s -> s.position == positionNode);
        this.signals.add(new Signal(name, positionNode, signal));
        this.synchronise();
    }

    public int getSignal(@NonNull BlockPos position, @NonNull Predicate<String> channelFilter) {
        var maxDistance = SharedConstants.MAX_RADIO_DISTANCE * SharedConstants.MAX_RADIO_DISTANCE;
        var signal = 0;

        for (var channel : signals) {
            if (!channelFilter.test(channel.name())) {
                continue;
            }

            var channelPosition = BlockPos.of(channel.position());
            var distance = channelPosition.distSqr(position);
            if (distance > maxDistance) {
                continue;
            }

            signal = Math.max(signal, channel.signal());
        }

        return signal;
    }

    @Override
    public void serverTick() {
        if (this.lazyTickCounter < 2) {
            this.lazyTickCounter++;
            return;
        }

        this.lazyTickCounter = 0;

        this.signals.removeIf(signal -> !this.level
                .getBlockState(BlockPos.of(signal.position))
                .hasBlockEntity()
        );
    }

    public record Signal(@NonNull String name, @NonNull Long position, int signal) {
        public static final Codec<Signal> CODEC = RecordCodecBuilder
                .create(i -> i
                        .group(
                                Codec.STRING.fieldOf("name").forGetter(Signal::name),
                                Codec.LONG.fieldOf("position").forGetter(Signal::position),
                                Codec.INT.fieldOf("signal").forGetter(Signal::signal)
                        )
                        .apply(i, Signal::new)
                );
    }
}
