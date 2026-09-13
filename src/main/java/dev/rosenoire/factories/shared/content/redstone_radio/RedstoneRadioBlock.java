package dev.rosenoire.factories.shared.content.redstone_radio;

import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import dev.rosenoire.factories.giddy.world.BlockHelper;
import dev.rosenoire.factories.giddy.world.OrientationPlacementType;
import dev.rosenoire.factories.giddy.world.ShapeHelper;
import dev.rosenoire.factories.shared.index.AllBlockStateProperties;
import dev.rosenoire.factories.shared.index.AllLevelComponentKeys;
import dev.rosenoire.factories.shared.index.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class RedstoneRadioBlock extends BaseEntityBlock {
    public static final MapCodec<RedstoneRadioBlock> CODEC = simpleCodec(RedstoneRadioBlock::new);
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty HAS_CHANNEL = AllBlockStateProperties.HAS_CHANNEL;
    private static final Map<FrontAndTop, VoxelShape> SHAPE = ShapeHelper
            .rotateFrontAndTop(ShapeHelper.rotate(
                    Block.column(16.0, 0.0, 2.0),
                    OctahedralGroup.BLOCK_ROT_X_90
            ));

    public RedstoneRadioBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                .setValue(POWERED, false)
                .setValue(HAS_CHANNEL, false)
        );
    }

    @Override
    protected @NonNull MapCodec<RedstoneRadioBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state,
                                           @NonNull BlockGetter level,
                                           @NonNull BlockPos pos,
                                           @NonNull CollisionContext context) {
        return SHAPE.get(state.getValue(ORIENTATION));
    }

    protected int getDelay(@NonNull BlockState state) {
        return 4;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ORIENTATION, HAS_CHANNEL, POWERED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(
                ORIENTATION,
                OrientationPlacementType.BLOCK_FACE.get(context)
        );
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state,
                                 @NonNull LevelReader level,
                                 @NonNull BlockPos pos) {
        var orientation = state.getValue(ORIENTATION);
        var neighbourPosition = pos.relative(orientation.front().getOpposite());
        var neighbourState = level.getBlockState(neighbourPosition);
        return neighbourState.isFaceSturdy(
                level, neighbourPosition, orientation.front(),
                SupportType.RIGID
        );
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NonNull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@NonNull BlockState state,
                                        @NonNull Level level,
                                        @NonNull BlockPos pos,
                                        @NonNull Direction direction) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof RedstoneRadioBlockEntity radio) {
            if (radio.getChannel().isEmpty()) {
                return 0;
            }

            var channel = radio.getChannel().get();
            var network = level.getComponent(AllLevelComponentKeys.RADIO_NETWORK);
            return network.getSignal(pos, s -> s.equals(channel));
        }

        return 0;
    }

    protected boolean shouldTurnOn(@NonNull Level level,
                                   @NonNull BlockPos pos,
                                   @NonNull BlockState state) {
        for (var direction : Direction.values()) {
            if (level.getSignal(pos.relative(direction), direction) > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void neighborChanged(@NonNull BlockState state,
                                   @NonNull Level level,
                                   @NonNull BlockPos pos,
                                   @NonNull Block block,
                                   @Nullable Orientation orientation,
                                   boolean movedByPiston) {
        if (state.canSurvive(level, pos)) {
            this.checkTickOnNeighbor(level, pos, state);
            return;
        }

        var blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (blockEntity instanceof RedstoneRadioBlockEntity radio) radio.onRemoved();
        dropResources(state, level, pos, blockEntity);
        level.removeBlock(pos, false);

        for (var direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }
    }

    protected void checkTickOnNeighbor(@NonNull Level level,
                                       @NonNull BlockPos pos,
                                       @NonNull BlockState state) {
        var on = state.getValue(POWERED);
        var shouldTurnOn = this.shouldTurnOn(level, pos, state);

        if (on != shouldTurnOn) {
            level.setBlock(pos, state.setValue(POWERED, shouldTurnOn), UPDATE_CLIENTS);
            level.scheduleTick(pos, this, this.getDelay(state));
        }

        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof RedstoneRadioBlockEntity radio) {
            radio.updateSignal();
        }
    }

    @Override
    protected boolean isSignalSource(@NonNull BlockState state) {
        return true;
    }

    @Override
    public void setPlacedBy(@NonNull Level level,
                            @NonNull BlockPos pos,
                            @NonNull BlockState state,
                            @Nullable LivingEntity by,
                            @NonNull ItemStack itemStack) {
        if (!this.shouldTurnOn(level, pos, state)) {
            return;
        }

        level.scheduleTick(pos, this, 1);
    }

    @Override
    protected void onPlace(@NonNull BlockState state,
                           @NonNull Level level,
                           @NonNull BlockPos pos,
                           @NonNull BlockState oldState,
                           boolean movedByPiston) {
        BlockHelper.updateNeighborsInFrontOfBlockWithOrientation(this, level, pos, state);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NonNull BlockState state,
                                               @NonNull ServerLevel level,
                                               @NonNull BlockPos pos,
                                               boolean movedByPiston) {
        if (movedByPiston) {
            return;
        }

        BlockHelper.updateNeighborsInFrontOfBlockWithOrientation(this, level, pos, state);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition,
                                                @NonNull BlockState blockState) {
        return new RedstoneRadioBlockEntity(worldPosition, blockState);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack,
                                                   @NonNull BlockState state,
                                                   @NonNull Level level,
                                                   @NonNull BlockPos pos,
                                                   @NonNull Player player,
                                                   @NonNull InteractionHand hand,
                                                   @NonNull BlockHitResult hitResult) {
        if (itemStack.is(Items.NAME_TAG) && itemStack.has(DataComponents.CUSTOM_NAME)) {
            var customName = itemStack.get(DataComponents.CUSTOM_NAME);
            if (customName == null) return InteractionResult.FAIL;
            var channelName = customName.getString();

            if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof RedstoneRadioBlockEntity radio) {
                var currentChannelName = radio.getChannel().orElse("");
                if (currentChannelName.equals(channelName)) return InteractionResult.PASS;
                radio.setChannel(channelName);

                level.playSound(
                        null, pos,
                        AllSoundEvents.REDSTONE_RADIO_CHANGE_CHANNEL, SoundSource.BLOCKS,
                        1f, Mth.nextFloat(level.getRandom(), 0.9f, 1.1f)
                );

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected void tick(@NonNull BlockState state,
                        @NonNull ServerLevel level,
                        @NonNull BlockPos pos,
                        @NonNull RandomSource random) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof RedstoneRadioBlockEntity radio) {
            level.updateNeighborsAt(pos, this);
        }

        // This is to make sure that the block updates when a potential emitter sends a
        // redstone signal. Otherwise, we can't know that for sure.
        level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
    }
}
