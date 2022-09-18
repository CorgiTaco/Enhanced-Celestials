package corgitaco.enhancedcelestials.world.level.levelgen.structure.crater;

import corgitaco.enhancedcelestials.util.FastNoise;
import corgitaco.enhancedcelestials.world.level.levelgen.structure.ECStructurePieceTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class CraterPiece extends StructurePiece {

    protected static FastNoise fastNoise = Util.make(new FastNoise(20202), (noise) -> {
        noise.SetNoiseType(FastNoise.NoiseType.Simplex);
        noise.SetFrequency(0.004F);
    });


    private final CraterStructure.PieceStructureInfo structureInfo;

    protected CraterPiece(CraterStructure.PieceStructureInfo structureInfo, int genDepth, BoundingBox boundingBox) {
        super(ECStructurePieceTypes.CRATER_PIECE.get(), genDepth, boundingBox);
        this.structureInfo = structureInfo;
    }

    public CraterPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(ECStructurePieceTypes.CRATER_PIECE.get(), tag);
        this.structureInfo = CraterStructure.PieceStructureInfo.CODEC.decode(NbtOps.INSTANCE, tag.get("volcano_info")).result().orElseThrow().getFirst();
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        compoundTag.put("volcano_info", CraterStructure.PieceStructureInfo.CODEC.encodeStart(NbtOps.INSTANCE, this.structureInfo).result().orElseThrow());
    }


    @Override
    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {

        double radius = this.structureInfo.baseRadiusX();
        double radiusZ = this.structureInfo.baseRadiusZ();

        double yRadius = 65;
        BlockPos origin = this.structureInfo.origin();
        int baseHeight = origin.getY();

        BlockPos subtract = origin.offset(-radius, 0, -radiusZ);
        int startX = subtract.getX();
        int startZ = subtract.getZ();

        BlockPos add = origin.offset(radius, 0, radiusZ);
        int endX = add.getX();
        int endZ = add.getZ();

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int minX = Math.max(startX - 10, chunkPos.getMinBlockX());
        int maxX = Math.min(endX + 10, chunkPos.getMaxBlockX());

        int minZ = Math.max(startZ - 10, chunkPos.getMinBlockZ());
        int maxZ = Math.min(endZ + 10, chunkPos.getMaxBlockZ());

        double xRadiusSquared = radius * radius;
        double yRadiusSquared = yRadius * yRadius;
        double zRadiusSquared = radiusZ * radiusZ;

        FastNoise fastNoise = new FastNoise(20202);
        fastNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
        fastNoise.SetFrequency(0.008F);


        for (int worldX = minX; worldX <= maxX; worldX++) {
            int localX = worldX - origin.getX();

            for (int worldZ = minZ; worldZ <= maxZ; worldZ++) {
                int localZ = worldZ - origin.getZ();

                for (double y = -yRadius; y <= yRadius; y++) {
                    mutable.set(worldX, baseHeight + y, worldZ);

                    //Credits to Hex_26 for this equation!
                    double equationResult = (localX * localX) / xRadiusSquared + (y * y) / yRadiusSquared + (localZ * localZ) / zRadiusSquared;
                    double threshold = 1 + 0.7 * fastNoise.GetNoise(mutable.getX(),  mutable.getZ());
                    if (equationResult >= threshold) {
                        continue;
                    }

                    worldGenLevel.setBlock(mutable, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }
}