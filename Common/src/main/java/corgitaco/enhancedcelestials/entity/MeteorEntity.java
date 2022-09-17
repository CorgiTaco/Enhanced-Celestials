package corgitaco.enhancedcelestials.entity;

import corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class MeteorEntity extends Entity {

    public MeteorEntity(Level level) {
        this(ECEntities.METEOR.get(), level);
    }

    public MeteorEntity(EntityType<? extends MeteorEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

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
        setDeltaMovement(velocity.subtract(0.0D, gravity, 0.0D));

        if (!level.isClientSide && (onGround || verticalCollision || horizontalCollision)) {
            discard();
            level.explode(this, getX(), getY(), getZ(), 4.0F, Explosion.BlockInteraction.DESTROY);
        }

        if (level.isClientSide) {

        }
    }
}