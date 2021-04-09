package corgitaco.enchancedcelestials.lunarevent;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public abstract class LunarEvent {
    private final String id;
    private final String name;
    private final double chance;

    public LunarEvent(String id, String name, double chance) {
        this.id = id;
        this.name = name;
        this.chance = chance;
    }

    public final String getID() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final double getChance() {
        return chance;
    }

    public double getSpawnCapacityMultiplier() {
        return 1.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean modifySkyLightMapColor(Vector3f originalMoonColor) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifyMoonColor() {
        return new Color(255, 255, 255, 255);
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifySkyColor(Color originalSkyColor) {
        return originalSkyColor;
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifyFogColor(Color originalSkyColor) {
        return originalSkyColor;
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifyWaterColor(Color originalWaterColor) {
        return originalWaterColor;
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifyWaterFogColor(Color originalWaterFogColor) {
        return originalWaterFogColor;
    }

    @OnlyIn(Dist.CLIENT)
    public Color modifyCloudColor(Color originalCloudColor) {
        return originalCloudColor;
    }

    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState) {
    }

    public void multiplyDrops(ServerWorld world, ItemStack itemStack) {
    }

    public void sendRisingNotification(PlayerEntity player) {
    }

    public void sendSettingNotification(PlayerEntity player) {
    }
}