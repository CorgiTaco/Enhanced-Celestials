package corgitaco.enhancedcelestials.item;

import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MeteorStaffItem extends Item {
    public MeteorStaffItem(Item.Properties p_43140_) {
        super(p_43140_);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            MeteorEntity meteorEntity = new MeteorEntity(pLevel);
            HitResult hitResult = getPlayerPOVHitResult(100, pLevel, pPlayer, ClipContext.Fluid.NONE);
            meteorEntity.setPos(pPlayer.position());

            Vec3 normalize = hitResult.getLocation().subtract(pPlayer.position()).normalize();

            meteorEntity.setDeltaMovement(normalize.multiply(5D, 5, 5D));
            meteorEntity.setSize((byte) pLevel.random.nextInt(5, 25));


            pLevel.addFreshEntity(meteorEntity);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    protected static BlockHitResult getPlayerPOVHitResult(double dist, Level level, Player player, ClipContext.Fluid context) {
        float $$3 = player.getXRot();
        float $$4 = player.getYRot();
        Vec3 $$5 = player.getEyePosition();
        float $$6 = Mth.cos(-$$4 * 0.017453292F - 3.1415927F);
        float $$7 = Mth.sin(-$$4 * 0.017453292F - 3.1415927F);
        float $$8 = -Mth.cos(-$$3 * 0.017453292F);
        float $$9 = Mth.sin(-$$3 * 0.017453292F);
        float $$10 = $$7 * $$8;
        float $$12 = $$6 * $$8;
        Vec3 $$14 = $$5.add((double)$$10 * dist, (double)$$9 * dist, (double)$$12 * dist);
        return level.clip(new ClipContext($$5, $$14, ClipContext.Block.OUTLINE, context, player));
    }


}