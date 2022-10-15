package corgitaco.enhancedcelestials.item;

import corgitaco.enhancedcelestials.entity.MeteorStrikeEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MeteorStaffItem extends Item {
    public MeteorStaffItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            CompoundTag itemStackTag = itemstack.getOrCreateTag();
            if (!itemStackTag.contains("strike_id_for_charging", Tag.TAG_INT)) {
                HitResult hitResult = getPlayerPOVHitResult(100, level, null, 0F, 1, player, ClipContext.Fluid.NONE);
                if (hitResult.getType() != HitResult.Type.MISS) {
                    MeteorStrikeEntity meteorStrikeEntity = new MeteorStrikeEntity(level, hitResult.getLocation(), player);
                    itemStackTag.putInt("strike_id_for_charging", meteorStrikeEntity.getId());
                    meteorStrikeEntity.setReady(false);
                    level.addFreshEntity(meteorStrikeEntity);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            } else {
                player.startUsingItem(hand);
            }
        }


        return super.use(level, player, hand);
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int time) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("strike_id_for_charging", Tag.TAG_INT)) {
            Entity meteorStrikeToCharge = level.getEntity(tag.getInt("strike_id_for_charging"));
            if (meteorStrikeToCharge instanceof MeteorStrikeEntity meteorStrikeEntity) {
                meteorStrikeEntity.setReady(true);
            }
            tag.remove("strike_id_for_charging");
        }
        super.releaseUsing(stack, level, entity, time);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!level.isClientSide) {
            if (tag.contains("strike_id_for_charging", Tag.TAG_INT)) {
                Entity meteorStrikeToCharge = level.getEntity(tag.getInt("strike_id_for_charging"));
                if (meteorStrikeToCharge instanceof MeteorStrikeEntity meteorStrikeEntity) {
                    HitResult hitResult = getPlayerPOVHitResult(100, level, (raytrace) -> {
                        Vec3 velocity = entity.getEyePosition().subtract(raytrace).normalize().scale(-0.5);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.END_ROD, raytrace.x, raytrace.y, raytrace.z, 2, velocity.x, velocity.y, velocity.z, 0.01);
                        }

                    }, 5, 1F, entity, ClipContext.Fluid.NONE);
                    if (meteorStrikeEntity.getBoundingBox().contains(hitResult.getLocation()) && meteorStrikeEntity.getSize() < 10) {
                        meteorStrikeEntity.setSize(meteorStrikeEntity.getSize() + 0.1F);
                    } else {
                        tag.remove("strike_id_for_charging");
                        meteorStrikeEntity.setReady(true);
                    }
                } else {
                    tag.remove("strike_id_for_charging");
                }
            }
        }
        super.onUseTick(level, entity, stack, count);
    }

    protected static BlockHitResult getPlayerPOVHitResult(double dist, Level level, @Nullable Consumer<Vec3> rayCastPosition, float distanceToStart, float rayTracePrecision, LivingEntity player, ClipContext.Fluid context) {
        float $$3 = player.getXRot();
        float $$4 = player.getYRot();
        Vec3 eyePosition = player.getEyePosition();
        float $$6 = Mth.cos(-$$4 * 0.017453292F - 3.1415927F);
        float $$7 = Mth.sin(-$$4 * 0.017453292F - 3.1415927F);
        float $$8 = -Mth.cos(-$$3 * 0.017453292F);
        float $$9 = Mth.sin(-$$3 * 0.017453292F);
        float $$10 = $$7 * $$8;
        float $$12 = $$6 * $$8;

        if (rayCastPosition != null) {
            for (double i = distanceToStart; i < dist - 1; i += rayTracePrecision) {
                rayCastPosition.accept(eyePosition.add((double) $$10 * i, (double) $$9 * i, (double) $$12 * i));
            }

        }

        return level.clip(new ClipContext(eyePosition, eyePosition.add((double) $$10 * dist, (double) $$9 * dist, (double) $$12 * dist), ClipContext.Block.COLLIDER, context, player));
    }


}