package corgitaco.enhancedcelestials.entity;

import corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MeteorEntity extends Entity {


    public MeteorEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MeteorEntity(Level level) {
        super(ECEntities.METEOR.get(), level);

    }

    @Override
    public void tick() {
        super.tick();
        Vec3 velocity = getDeltaMovement();
        this.move(MoverType.SELF, velocity);
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));

        if (onGround && !level.isClientSide) {
            explode();
        }
    }

    @NotNull
    private void explode() {
        discard();
        this.level.explode(this, getX(), getY(), getZ(), 4.0F, Explosion.BlockInteraction.DESTROY);
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
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (Direction value : Direction.values()) {
            BlockState offsetState = level.getBlockState(mutableBlockPos.setWithOffset(this.blockPosition(), value));
            if (!offsetState.isAir()) {
                explode();
            }
        }
        if (!state.isAir()) {
            explode();
        }
        super.onInsideBlock(state);
    }
}
