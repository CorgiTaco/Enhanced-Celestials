package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HarvestMoon extends LunarEvent {

    static double cropGrowthMultiplier = EnhancedCelestialsConfig.harvestMoonCropGrowthChanceMultiplier.get();
    static double cropDropMultiplier = EnhancedCelestialsConfig.harvestMoonCropDropsMultiplier.get();
    static Set<Item> blacklistedCropDropMultiplierItems = new ObjectOpenHashSet<>(EnhancedCelestialsConfig.blacklistedCropDropItems.get().stream().map(ResourceLocation::new).filter((resourceLocation) -> (EnhancedCelestialsUtils.filterRegistryID(resourceLocation, Registry.ITEM, "Item"))).map(Registry.ITEM::getOptional).map(Optional::get).collect(Collectors.toSet()));
    static Set<Block> blacklistedCropGrowthMultiplierBlocks = new ObjectOpenHashSet<>(EnhancedCelestialsConfig.blacklistedCropGrowthBlocks.get().stream().map(ResourceLocation::new).filter((resourceLocation) -> (EnhancedCelestialsUtils.filterRegistryID(resourceLocation, Registry.BLOCK, "Block"))).map(Registry.BLOCK::getOptional).map(Optional::get).collect(Collectors.toSet()));

    static boolean isCropGrowthBlocksBlackList = EnhancedCelestialsConfig.cropGrowthBlocksBlacklist.get();
    static boolean isCropDropItemsBlackList = EnhancedCelestialsConfig.cropDropItemsBlacklist.get();

    public HarvestMoon() {
        super(LunarEventSystem.HARVEST_MOON_EVENT_ID, 0.025);
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
    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState, CallbackInfo ci) {

        boolean flag = isCropGrowthBlocksBlackList == blacklistedCropGrowthMultiplierBlocks.contains(block);

        if (flag) {
            if (BlockTags.CROPS.contains(block) || BlockTags.BEE_GROWABLES.contains(block) || BlockTags.SAPLINGS.contains(block)) {
                for (int i = 0; i <= cropGrowthMultiplier; i++) {
                    block.randomTick(blockState, world, pos, world.rand);
                }
            }
        }
    }

    @Override
    public void multiplyDrops(ServerWorld world, ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (isCropDropItemsBlackList) {
            if (!blacklistedCropDropMultiplierItems.contains(item)) {
                if (Tags.Items.CROPS.contains(item) || EnhancedCelestialsUtils.VEGETABLES.contains(item) || EnhancedCelestialsUtils.FRUITS.contains(item)) {
                    itemStack.setCount((int) (itemStack.getCount() * cropDropMultiplier));
                }
            }
        } else {
            if (blacklistedCropDropMultiplierItems.contains(item))
                itemStack.setCount((int) (itemStack.getCount() * cropDropMultiplier));
        }
    }
}
