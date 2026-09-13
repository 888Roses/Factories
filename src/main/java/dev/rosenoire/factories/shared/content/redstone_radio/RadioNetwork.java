package dev.rosenoire.factories.shared.content.redstone_radio;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rosenoire.factories.SharedConstants;
import dev.rosenoire.factories.shared.index.AllLevelComponentKeys;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v3.component.ComponentV3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class RadioNetwork implements ComponentV3, AutoSyncedComponent {
    private static final Codec<Object2IntOpenHashMap<Channel>> SIGNALS_CODEC = Codec
            .unboundedMap(Channel.CODEC, Codec.INT)
            .xmap(
                    Object2IntOpenHashMap::new,
                    signals -> signals
            );
    private static final String SIGNALS_KEY = "signals";

    private final @NonNull Level level;
    private final @NonNull Object2IntOpenHashMap<Channel> signals
            = new Object2IntOpenHashMap<>();

    public RadioNetwork(@NonNull Level level) {
        this.level = level;
    }

    public @NonNull Level getLevel() {
        return this.level;
    }

    @Override
    public void readData(@NonNull ValueInput readView) {
        this.signals.clear();
        readView.read(SIGNALS_KEY, SIGNALS_CODEC).ifPresent(this.signals::putAll);
    }

    @Override
    public void writeData(@NonNull ValueOutput writeView) {
        writeView.store(SIGNALS_KEY, SIGNALS_CODEC, this.signals);
    }

    public void synchronise() {
        AllLevelComponentKeys.RADIO_NETWORK.sync(this.getLevel());
    }

    public void store(@NonNull BlockPos pos, @NonNull String name, int signal) {
        var channel = new Channel(name, pos.asLong());
        this.signals.put(channel, signal);
        this.synchronise();
    }

    public int getSignal(@NonNull BlockPos position, @NonNull Predicate<String> channelFilter) {
        var maxDistance = SharedConstants.MAX_RADIO_DISTANCE * SharedConstants.MAX_RADIO_DISTANCE;
        var signal = 0;

        for (var entry : signals.object2IntEntrySet()) {
            var channel = entry.getKey();

            if (!channelFilter.test(channel.name())) {
                continue;
            }

            var channelPosition = BlockPos.of(channel.position());
            var distance = channelPosition.distSqr(position);
            if (distance > maxDistance) {
                continue;
            }

            var entrySignal = entry.getIntValue();
            signal = Math.max(signal, entrySignal);
        }

        return signal;
    }

    public record Channel(@NonNull String name, @NonNull Long position) {
        public static final Codec<Channel> CODEC = RecordCodecBuilder
                .create(i -> i
                        .group(
                                Codec.STRING.fieldOf("name").forGetter(Channel::name),
                                Codec.LONG.fieldOf("position").forGetter(Channel::position)
                        )
                        .apply(i, Channel::new)
                );
    }
}
