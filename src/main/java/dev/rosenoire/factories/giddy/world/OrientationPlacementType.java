package dev.rosenoire.factories.giddy.world;

import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public enum OrientationPlacementType {
    FACING(context -> {
        var front = context.getNearestLookingDirection().getOpposite();

        var top = switch (front) {
            case DOWN -> context.getHorizontalDirection().getOpposite();
            case UP -> context.getHorizontalDirection();
            case NORTH,
                 SOUTH,
                 WEST,
                 EAST -> Direction.UP;
        };

        return FrontAndTop.fromFrontAndTop(front, top);
    }),
    BLOCK_FACE(context -> {
        var front = context.getClickedFace();

        Direction top;
        if (front.getAxis().isVertical()) top = context.getHorizontalDirection();
        else top = Direction.UP;

        return FrontAndTop.fromFrontAndTop(front, top);
    }),
    ;

    private final @NonNull Function<BlockPlaceContext, FrontAndTop> function;

    OrientationPlacementType(@NonNull Function<BlockPlaceContext, FrontAndTop> function) {
        this.function = function;
    }

    public @NonNull FrontAndTop get(@NonNull BlockPlaceContext context) {
        return this.function.apply(context);
    }
}
