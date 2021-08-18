package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.helper.LevelGetter;
import corgitaco.enhancedcelestials.mixin.access.WorldEntitySpawnerAccess;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldEntitySpawner.EntityDensityManager.class)
public class MixinEntityDensityManager implements LevelGetter {

    @Shadow @Final private int field_234981_a_;
    @Shadow @Final private Object2IntMap<EntityClassification> field_234984_d_;
    ServerWorld level;

    @Override
    public void setLevel(World world) {
        this.level = (ServerWorld) world;
    }

    @Override
    public World getLevel() {
        return this.level;
    }

    @Inject(method = "func_234991_a_", at = @At("HEAD"), cancellable = true)
    private void modifySpawnCapByCategory(EntityClassification entityClassification, CallbackInfoReturnable<Boolean> cir) {
        if (this.level != null) {
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) this.level).getLunarContext();
            if (lunarContext != null) {
                int i = (int) (entityClassification.getMaxNumberOfCreature() * (this.field_234981_a_ * lunarContext.getCurrentEvent().getSpawnMultiplierForMonsterCategory(entityClassification)) / WorldEntitySpawnerAccess.getMagicNumber());
                cir.setReturnValue(this.field_234984_d_.getInt(entityClassification) < i);
            }
        }
    }
}
