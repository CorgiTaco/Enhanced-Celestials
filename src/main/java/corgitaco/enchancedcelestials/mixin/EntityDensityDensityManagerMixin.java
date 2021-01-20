package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.misc.AdditionalEntityDensityManagerData;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldEntitySpawner.EntityDensityManager.class)
public class EntityDensityDensityManagerMixin implements AdditionalEntityDensityManagerData {
	private boolean isOverworld;

	@Shadow @Final private int field_234981_a_;

	@Shadow @Final private Object2IntOpenHashMap<EntityClassification> field_234982_b_;

	@Inject(at = @At("RETURN"), method = "func_234991_a_", cancellable = true)
	private void modifySpawnCap(EntityClassification entityClassification, CallbackInfoReturnable<Boolean> cir) {
		if (isOverworld)
			EnhancedCelestialsUtils.modifySpawnCap(entityClassification, this.field_234981_a_, this.field_234982_b_, cir);
	}

	@Override
	public void setIsOverworld(boolean isOverworld) {
		this.isOverworld = isOverworld;
	}
}
