package corgitaco.enhancedcelestials.entity;

import corgitaco.enhancedcelestials.core.ECBlocks;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class SpaceMossBugEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> COVERED_IN_SPORES = SynchedEntityData.defineId(SpaceMossBugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> SPORE_DELAY = SynchedEntityData.defineId(SpaceMossBugEntity.class, EntityDataSerializers.BYTE);

    public SpaceMossBugEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(COVERED_IN_SPORES, false);
        entityData.define(SPORE_DELAY, (byte) 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        setCoveredInSpores($$0.getBoolean("CoveredInSpores"));
        setSporeDelay($$0.getByte("SporeDelay"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("CoveredInSpores", isCoveredInSpores());
        $$0.putByte("SporeDelay", getSporeDelay());
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide) {
            if (tickCount % 20 == 0) {
                var sporeDelay = getSporeDelay();
                if (sporeDelay > 0) {
                    setSporeDelay((byte) (sporeDelay - 1));
                }
            }

            if (isCoveredInSpores() && getBlockStateOn().is(EnhancedCelestialsBlockTags.SPACE_MOSS_GROWS_ON) && random.nextInt(48) == 0) {
                var blockPos = new BlockPos(getBlockX(), getBlockY(), getBlockZ());

                var blockState = (random.nextBoolean() ? ECBlocks.SPACE_MOSS_CARPET : ECBlocks.SPACE_MOSS_GRASS).get().defaultBlockState();

                if (blockState.canSurvive(level, blockPos) && level.setBlock(blockPos, blockState, Block.UPDATE_CLIENTS)) {
                    setCoveredInSpores(false);

                    setSporeDelay((byte) 254);
                }
            }
        }
    }

    public boolean isCoveredInSpores() {
        return entityData.get(COVERED_IN_SPORES);
    }

    // 100+ Years. Still waiting for a sequel to Spore
    public byte getSporeDelay() {
        return entityData.get(SPORE_DELAY);
    }

    public void setCoveredInSpores(boolean b) {
        entityData.set(COVERED_IN_SPORES, b);
    }

    public void setSporeDelay(byte b) {
        entityData.set(SPORE_DELAY, b);
    }
}
