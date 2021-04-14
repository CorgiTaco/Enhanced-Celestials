package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.awt.*;

public class HarvestMoon extends LunarEvent {
    private static double cropGrowthMultiplier = EnhancedCelestialsConfig.harvestMoonCropGrowthChanceMultiplier.get();
    private static double cropDropMultiplier = EnhancedCelestialsConfig.harvestMoonCropDropsMultiplier.get();
    private static boolean displayNotification = EnhancedCelestialsConfig.harvestMoonNotification.get();

    public HarvestMoon() {
        super(LunarEventSystem.HARVEST_MOON_EVENT_ID, EnhancedCelestialsConfig.harvestMoonChance.get());
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f originalLightmapColor) {
        originalLightmapColor.lerp(EnhancedCelestialsClientUtils.transformToVectorColor(new Color(255, 219, 99)), 1.0F);
        return true;
    }

    @Override
    public Color modifyMoonColor() {
        return new Color(255, 219, 99, 255);
    }

    @Override
    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState) {
        for (int i = 0; i < cropGrowthMultiplier; i++) {
            if (i > 0) {
                blockState = world.getBlockState(pos);
                block = blockState.getBlock();
            }
            if (!EnhancedCelestialsUtils.HARVEST_MOON_BLACKLISTED_CROP_GROWTH.contains(block)) {
                if (EnhancedCelestialsUtils.HARVEST_MOON_WHITELISTED_CROP_GROWTH.contains(block)) {
                    block.randomTick(blockState, world, pos, world.rand);
                }
            }
        }
    }

    @Override
    public void multiplyDrops(ServerWorld world, ItemStack itemStack) {
        Item item = itemStack.getItem();
        try {
            if (!EnhancedCelestialsUtils.HARVEST_MOON_BLACKLISTED_CROP_DROPS.isDefaulted() && !EnhancedCelestialsUtils.HARVEST_MOON_WHITELISTED_CROP_DROPS.isDefaulted()) {
                if (!EnhancedCelestialsUtils.HARVEST_MOON_BLACKLISTED_CROP_DROPS.contains(item)) {
                    if (EnhancedCelestialsUtils.HARVEST_MOON_WHITELISTED_CROP_DROPS.contains(item)) {
                        itemStack.setCount((int) (itemStack.getCount() * cropDropMultiplier));
                    }
                }
            }
        } catch (IllegalStateException e) {
            //Silently fail
        }

    }

    @Override
    public void sendRisingNotification(PlayerEntity player) {
        if (displayNotification) {
            TranslationTextComponent component = new TranslationTextComponent("enhancedcelestials.notification.harvest_moon.rise");
            component.setStyle(component.getStyle().applyFormatting(TextFormatting.GOLD));
            player.sendMessage(component, Util.DUMMY_UUID);
        }
    }

    @Override
    public void sendSettingNotification(PlayerEntity player) {
        if (displayNotification) {
            TranslationTextComponent component = new TranslationTextComponent("enhancedcelestials.notification.harvest_moon.set");
            component.setStyle(component.getStyle().applyFormatting(TextFormatting.GOLD));
            player.sendMessage(component, Util.DUMMY_UUID);
        }
    }
}