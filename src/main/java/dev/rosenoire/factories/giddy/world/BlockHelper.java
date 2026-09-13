package dev.rosenoire.factories.giddy.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public final class BlockHelper {
    private BlockHelper() {
    }

    public static <B extends Block> void updateNeighborsInFront(
            @NonNull B changedBlock,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull BlockState state,
            @NonNull Direction up
    ) {
        Objects.requireNonNull(changedBlock);
        Objects.requireNonNull(level);
        Objects.requireNonNull(pos);
        Objects.requireNonNull(state);
        Objects.requireNonNull(up);

        if (!state.hasProperty(FACING)) {
            return;
        }

        var direction = state.getValue(FACING);
        var oppositePos = pos.relative(direction.getOpposite());
        var orientation = ExperimentalRedstoneUtils
                .initialOrientation(level, direction.getOpposite(), up);

        level.neighborChanged(oppositePos, changedBlock, orientation);
        level.updateNeighborsAtExceptFromFacing(oppositePos, changedBlock, direction,
                orientation);
    }

    public static <B extends Block> void updateNeighborsInFrontOfBlockWithOrientation(
            @NonNull B changedBlock,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull BlockState state
    ) {
        Objects.requireNonNull(changedBlock);
        Objects.requireNonNull(level);
        Objects.requireNonNull(pos);
        Objects.requireNonNull(state);

        if (!state.hasProperty(ORIENTATION)) {
            return;
        }

        var orientation = state.getValue(ORIENTATION);
        var oppositePos = pos.relative(orientation.front().getOpposite());
        var redstoneOrientation = ExperimentalRedstoneUtils
                .initialOrientation(level, orientation.front().getOpposite(), orientation.top());

        level.neighborChanged(oppositePos, changedBlock, redstoneOrientation);
        level.updateNeighborsAtExceptFromFacing(
                oppositePos,
                changedBlock,
                orientation.front(),
                redstoneOrientation
        );
    }
}
