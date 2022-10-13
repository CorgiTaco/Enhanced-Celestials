package corgitaco.enhancedcelestials.entity;

import com.google.common.annotations.VisibleForTesting;
import corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class MeteorEntity extends Entity {
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.FLOAT);

    public MeteorEntity(Level level) {
        this(ECEntities.METEOR.get(), level);
    }

    public MeteorEntity(EntityType<? extends MeteorEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(SIZE, 1F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setSize(compoundTag.getFloat("Size"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("Size", getSize());
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();


        var velocity = getDeltaMovement();

        setDeltaMovement(velocity.subtract(0, 0.3, 0));
        move(MoverType.SELF, velocity);

        if (!level.isClientSide) {
            if (onGround || verticalCollision || horizontalCollision) {
                discard();
                level.explode(this, getX(), getY(), getZ(), 5F, Explosion.BlockInteraction.DESTROY);
            }
        } else {
            Vec3 reverse = getDeltaMovement().multiply(-1, -1, -1);

            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.FLAME, getX() + Mth.nextDouble(random, -0.4, 0.4), getY() + Mth.nextDouble(random, -0.4, 0.4), getZ() + Mth.nextDouble(random, -0.4, 0.4), reverse.x(), reverse.y(), reverse.z());
            }
        }
    }

    public float getSize() {
        return Math.max(entityData.get(SIZE), 1);
    }

    @VisibleForTesting
    public void setSize(float pSize) {
        float i = Mth.clamp(pSize, 1F, Byte.MAX_VALUE);
        this.entityData.set(SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (SIZE.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return super.getDimensions(pPose).scale(getSize() * 0.5F);
    }

}