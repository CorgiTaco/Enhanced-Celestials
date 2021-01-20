package corgitaco.enchancedcelestials.lunarevent;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.awt.*;

public abstract class LunarEvent {
    private final String id;
    private final double chance;

    public LunarEvent(String id, double chance) {
        this.id = id;
        this.chance = chance;
    }

    public final String getID() {
        return id;
    }

    public final double getChance() {
        return chance;
    }

    public void multiplySpawnCap(EntityClassification mobCategory, int spawningChunkCount, Object2IntOpenHashMap<EntityClassification> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
    }

    @OnlyIn(Dist.CLIENT)
    public boolean modifySkyLightMapColor(Vector3f originalMoonColor) {
        return false;
    }

    public boolean stopSleeping(PlayerEntity playerEntity) {
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

    public TranslationTextComponent successTranslationTextComponent() {
        return new TranslationTextComponent("enhancedcelestials.commands.success." + id.toLowerCase());
    }

    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState, CallbackInfo ci) {
    }
}
