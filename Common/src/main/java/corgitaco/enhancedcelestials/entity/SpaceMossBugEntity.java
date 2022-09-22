package corgitaco.enhancedcelestials.entity;

import corgitaco.enhancedcelestials.core.ECBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class SpaceMossBugEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> COVERED_IN_SPORES = SynchedEntityData.defineId(SpaceMossBugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> SPORE_DELAY = SynchedEntityData.defineId(SpaceMossBugEntity.class, EntityDataSerializers.BYTE);

    public SpaceMossBugEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(2, new RandomLookAroundGoal(this));
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

            if (isCoveredInSpores() && random.nextInt(96) == 0) {
                var blockState = (random.nextBoolean() ? ECBlocks.SPACE_MOSS_CARPET : ECBlocks.SPACE_MOSS_GRASS).get().defaultBlockState();

                var blockPos = blockPosition();

                if (level.isEmptyBlock(blockPos) && blockState.canSurvive(level, blockPos) && level.setBlock(blockPos, blockState, Block.UPDATE_CLIENTS)) {
                    setCoveredInSpores(false);
                    setSporeDelay((byte) 254);
                }
            }
        }

        if (level.isClientSide && isCoveredInSpores()) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.FALLING_NECTAR,
                        getX() + Mth.nextDouble(random, -0.4D, 0.4D),
                        getY() + Mth.nextDouble(random, -0.4D, 0.4D),
                        getZ() + Mth.nextDouble(random, -0.4D, 0.4D),
                        0.0D,
                        0.0D,
                        0.0D);
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

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }
}
