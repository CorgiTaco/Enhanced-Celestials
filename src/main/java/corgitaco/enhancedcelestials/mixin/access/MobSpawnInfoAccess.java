package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

@Mixin(MobSpawnSettings.class)
public interface MobSpawnInfoAccess {

    @Invoker("<init>")
    static MobSpawnSettings create(float creatureSpawnProbability, Map<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners, Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts, boolean isValidSpawnBiomeForPlayer) {
        throw new Error("Mixin did not apply!");
    }

    @Accessor
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();

    @Accessor
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();
}
