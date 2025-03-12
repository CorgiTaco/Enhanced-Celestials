package dev.corgitaco.enhancedcelestials.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private Object halfPrice(Object original) {
        Level world = this.player.level();
        Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(world);

        if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
            return original;
        } else {
            EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();
            return ((Double) (((Integer) original) * data.currentLunarEvent().anvilCostAmplifier())).intValue();
        }
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
    private Object halfPrice2(Object original) {
        Level world = this.player.level();
        Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(world);
        if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
            return original;
        } else {
            EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();
            return ((Double) (((Integer) original) * data.currentLunarEvent().anvilCostAmplifier())).intValue();
        }
    }
}
