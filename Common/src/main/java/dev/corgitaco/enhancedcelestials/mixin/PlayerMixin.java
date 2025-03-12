package dev.corgitaco.enhancedcelestials.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapMethod(method = "giveExperiencePoints")
    private void modifyXPPoints(int xpPoints, Operation<Void> original) {
        Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(this.level());
        if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
            original.call(xpPoints);
            return;
        }

        EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();
        double xp = data.currentLunarEvent().xpAmplifier();
        original.call((int) (xp * xpPoints));
    }
}
