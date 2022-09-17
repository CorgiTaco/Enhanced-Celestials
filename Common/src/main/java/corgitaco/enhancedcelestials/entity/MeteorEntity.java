package corgitaco.enhancedcelestials.entity;

import corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class MeteorEntity extends Entity {
    private static final EntityDataAccessor<Byte> SIZE = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BYTE);

    public MeteorEntity(Level level) {
        this(ECEntities.METEOR.get(), level);
    }

    public MeteorEntity(EntityType<? extends MeteorEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(SIZE, (byte) 1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setSize(compoundTag.getByte("Size"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putByte("Size", getSize());
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        var gravity = 0.2D;

        var velocity = getDeltaMovement();
        move(MoverType.SELF, velocity);

        if (isNoGravity()) {
            return;
        }
        setDeltaMovement(velocity.subtract(0.0D, gravity, 0.0D));

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

    public byte getSize() {
        return (byte) Math.max(entityData.get(SIZE), 1);
    }

    public void setSize(byte b) {
        entityData.set(SIZE, (byte) Math.max(b, 1));
    }
}