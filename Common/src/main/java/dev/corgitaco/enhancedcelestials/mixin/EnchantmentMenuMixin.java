package dev.corgitaco.enhancedcelestials.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin extends AbstractContainerMenu {


    protected EnchantmentMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @ModifyExpressionValue(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentCost(Lnet/minecraft/util/RandomSource;IILnet/minecraft/world/item/ItemStack;)I"))
    private int modifyCost(int original, ItemStack stack, Level level, BlockPos pos) {

        Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(level);
        if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
            return original;
        }

        EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();
        double xp = data.currentLunarEvent().enchantmentTableCostAmplifier();
        return (int) (original * xp);
    }

    @WrapMethod(method = "getEnchantmentList")
    private List<EnchantmentInstance> modifyEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost, Operation<List<EnchantmentInstance>> original) {
        Player player = null;
        for (Slot slot1 : this.slots) {
            if (slot1.container instanceof Inventory inventory) {
                player = inventory.player;
                break;
            }
        }

        if (player != null) {
            Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(player.level());
            if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
                return original.call(registryAccess, stack, slot, cost);
            }
            EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();
            double enchantmentTableCostAmplifier = data.currentLunarEvent().enchantmentTableCostAmplifier();
            int ogCost = (int) (cost / enchantmentTableCostAmplifier);
            return original.call(registryAccess, stack, slot, ogCost);
        }

        return original.call(registryAccess, stack, slot, cost);
    }
}
