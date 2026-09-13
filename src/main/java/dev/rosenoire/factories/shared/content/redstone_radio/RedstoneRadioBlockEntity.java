package dev.rosenoire.factories.shared.content.redstone_radio;

import com.mojang.logging.LogUtils;
import dev.rosenoire.factories.shared.index.AllBlockEntityTypes;
import dev.rosenoire.factories.shared.index.AllBlockStateProperties;
import dev.rosenoire.factories.shared.index.AllLevelComponentKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RedstoneRadioBlockEntity extends BlockEntity {
    public final Logger log = LogUtils.getLogger();
    protected static final String CHANNEL_NAME_KEY = "channel_name";

    protected @NonNull Optional<String> channel = Optional.empty();

    public RedstoneRadioBlockEntity(@NonNull BlockEntityType<?> type,
                                    @NonNull BlockPos worldPosition,
                                    @NonNull BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    public RedstoneRadioBlockEntity(@NonNull BlockPos worldPosition,
                                    @NonNull BlockState blockState) {
        this(AllBlockEntityTypes.REDSTONE_RADIO, worldPosition, blockState);
    }

    public @NonNull Optional<String> getChannel() {
        return this.channel;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void setChannel(@Nullable String channel) {
        this.channel = Optional.ofNullable(channel);
        this.setChanged();

        if (this.level != null) {
            var hasChannel = channel != null;
            this.level.setBlock(
                    this.getBlockPos(),
                    this.getBlockState().setValue(AllBlockStateProperties.HAS_CHANNEL, hasChannel),
                    RedstoneRadioBlock.UPDATE_CLIENTS
            );
        }
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.channel = input.getString(CHANNEL_NAME_KEY);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        this.channel.ifPresent(_channel ->
                output.putString(CHANNEL_NAME_KEY, _channel)
        );
    }

    public void updateSignal() {
        if (this.channel.isEmpty()) {
            return;
        }

        var level = this.getLevel();
        if (level == null) return;

        var network = level.getComponent(AllLevelComponentKeys.RADIO_NETWORK);
        var pos = this.getBlockPos();

        network.store(pos, this.channel.get(), level.getBestNeighborSignal(pos));
    }

    public void onRemoved() {
        if (this.channel.isEmpty()) {
            return;
        }

        var level = this.getLevel();
        if (level == null) return;
        var network = level.getComponent(AllLevelComponentKeys.RADIO_NETWORK);
        var pos = this.getBlockPos();
        network.store(pos, this.channel.get(), 0);

        this.log.warn(
                "Removed block entity at: {} with channel: {}",
                pos.toShortString(),
                this.channel.get()
        );
    }
}
