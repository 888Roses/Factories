package dev.rosenoire.factories.giddy.world;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

import static com.mojang.math.OctahedralGroup.*;

public final class ShapeHelper {
    private static final Map<FrontAndTop, OctahedralGroup[]> ORIENTATION_TRANSFORMATION_MAP
            = ImmutableMap.<FrontAndTop, OctahedralGroup[]>builder()
            .put(FrontAndTop.DOWN_EAST, new OctahedralGroup[]{BLOCK_ROT_X_90, BLOCK_ROT_Y_90})
            .put(FrontAndTop.DOWN_NORTH, new OctahedralGroup[]{BLOCK_ROT_X_90})
            .put(FrontAndTop.DOWN_SOUTH, new OctahedralGroup[]{BLOCK_ROT_X_90, BLOCK_ROT_Y_180})
            .put(FrontAndTop.DOWN_WEST, new OctahedralGroup[]{BLOCK_ROT_X_90, BLOCK_ROT_Y_270})
            .put(FrontAndTop.EAST_UP, new OctahedralGroup[]{BLOCK_ROT_Y_90})
            .put(FrontAndTop.NORTH_UP, new OctahedralGroup[0])
            .put(FrontAndTop.SOUTH_UP, new OctahedralGroup[]{BLOCK_ROT_Y_180})
            .put(FrontAndTop.WEST_UP, new OctahedralGroup[]{BLOCK_ROT_Y_270})
            .put(FrontAndTop.UP_EAST, new OctahedralGroup[]{BLOCK_ROT_X_270, BLOCK_ROT_Y_270})
            .put(FrontAndTop.UP_NORTH, new OctahedralGroup[]{BLOCK_ROT_X_270, BLOCK_ROT_Y_180})
            .put(FrontAndTop.UP_SOUTH, new OctahedralGroup[]{BLOCK_ROT_X_270})
            .put(FrontAndTop.UP_WEST, new OctahedralGroup[]{BLOCK_ROT_X_270, BLOCK_ROT_Y_90})
            .build();

    private ShapeHelper() {
    }

    public static @NonNull Map<FrontAndTop, VoxelShape> rotateFrontAndTop(
            @NonNull VoxelShape voxelShape
    ) {
        var map = new HashMap<FrontAndTop, VoxelShape>(FrontAndTop.values().length, 1f);
        for (var transformation : ORIENTATION_TRANSFORMATION_MAP.entrySet()) {
            map.put(transformation.getKey(), rotate(voxelShape, transformation.getValue()));
        }

        return map;
    }

    public static @NonNull VoxelShape rotate(@NonNull VoxelShape voxelShape,
                                             @NonNull OctahedralGroup... rotations) {
        for (var rotation : rotations) {
            voxelShape = Shapes.rotate(voxelShape, rotation);
        }

        return voxelShape;
    }
}
