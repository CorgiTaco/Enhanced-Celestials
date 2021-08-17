package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

@Mixin(MobSpawnInfo.class)
public interface MobSpawnInfoAccess {

    @Invoker("<init>")
    static MobSpawnInfo create(float creatureSpawnProbability, Map<EntityClassification, List<MobSpawnInfo.Spawners>> spawners, Map<EntityType<?>, MobSpawnInfo.SpawnCosts> spawnCosts, boolean isValidSpawnBiomeForPlayer) {
        throw new Error("Mixin did not apply!");
    }

    @Accessor
    Map<EntityType<?>, MobSpawnInfo.SpawnCosts> getSpawnCosts();

    @Accessor
    Map<EntityClassification, List<MobSpawnInfo.Spawners>> getSpawners();
}
