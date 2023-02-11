package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.entity.condition.Condition;
import corgitaco.corgilib.entity.condition.ConditionContext;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class LunarEvent {

    public static final Codec<LunarEvent> DIRECT_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(ResourceKey.codec(Registry.DIMENSION_REGISTRY), SpawnRequirements.CODEC).fieldOf("dimension_chances").forGetter(LunarEvent::getEventChancesByDimension),
                    LunarEventClientSettings.CODEC.fieldOf("client_settings").forGetter(LunarEvent::getClientSettings),
                    LunarTextComponents.CODEC.fieldOf("text_components").forGetter(LunarEvent::getTextComponents),
                    LunarMobSettings.CODEC.fieldOf("mob_settings").forGetter(LunarEvent::getLunarMobSettings),
                    DropSettings.CODEC.fieldOf("drops").forGetter(LunarEvent::getDropSettings)
            ).apply(builder, LunarEvent::new)
    );

    private final Map<ResourceKey<Level>, SpawnRequirements> eventChancesByDimension;
    private final LunarEventClientSettings clientSettings;
    private final LunarTextComponents textComponents;
    private final LunarMobSettings lunarMobSettings;
    private final DropSettings dropSettings;

    public LunarEvent(Map<ResourceKey<Level>, SpawnRequirements> eventChancesByDimension, LunarEventClientSettings clientSettings, LunarTextComponents textComponents, LunarMobSettings lunarMobSettings, DropSettings dropSettings) {
        this.eventChancesByDimension = eventChancesByDimension;
        this.clientSettings = clientSettings;
        this.textComponents = textComponents;
        this.lunarMobSettings = lunarMobSettings;
        this.dropSettings = dropSettings;
    }

    public void onBlockItemDrop(ServerLevel world, ItemStack itemStack) {
        this.dropSettings.dropEnhancer().forEach((itemTagKey, multiplier) -> {
            if (itemStack.is(itemTagKey)) {
                itemStack.setCount((int) Math.round(itemStack.getCount() * multiplier));
            }
        });
    }

    @Nullable
    public LunarTextComponents.Notification startNotification() {
        return this.textComponents.riseNotification().orElse(null);
    }

    @Nullable
    public LunarTextComponents.Notification endNotification() {
        return this.textComponents.setNotification().orElse(null);
    }

    public LunarEventClientSettings getClientSettings() {
        return clientSettings;
    }

    public double getSpawnMultiplierForMonsterCategory(MobCategory classification) {
        return this.lunarMobSettings.spawnCategoryMultiplier().getOrDefault(classification, 1.0D);
    }

    public void livingEntityTick(LivingEntity entity) {
        this.lunarMobSettings.effectsForEntityTag().forEach((entityFilterMapPair) -> {
            Condition entityFilter = entityFilterMapPair.getFirst();
            if (entityFilter.passes(new ConditionContext(entity.level, entity, entity.isDeadOrDying(), 0))) {
                MobEffectInstanceBuilder builder = entityFilterMapPair.getSecond();
                entity.addEffect(builder.makeInstance());
            }
        });
    }

    public LunarMobSpawnInfo getLunarSpawner() {
        return this.lunarMobSettings.lunarMobSpawnInfo();
    }

    public boolean blockSleeping(LivingEntity entity) {
        return this.lunarMobSettings.blockSleeping().passes(new ConditionContext(entity.level, entity, entity.isDeadOrDying(), 0));
    }

    public LunarTextComponents getTextComponents() {
        return textComponents;
    }

    public LunarMobSettings getLunarMobSettings() {
        return lunarMobSettings;
    }

    public DropSettings getDropSettings() {
        return dropSettings;
    }

    public Map<ResourceKey<Level>, SpawnRequirements> getEventChancesByDimension() {
        return eventChancesByDimension;
    }

    public record SpawnRequirements(double chance, int minNumberOfNights, IntArraySet validMoonPhases) {
        public static final Codec<SpawnRequirements> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        Codec.DOUBLE.fieldOf("chance").forGetter(SpawnRequirements::chance),
                        Codec.INT.fieldOf("min_number_of_nights_between").forGetter(SpawnRequirements::minNumberOfNights),
                        Codec.list(Codec.intRange(0, 8)).fieldOf("valid_moon_phases").forGetter((clientSettings) -> new ArrayList<>(clientSettings.validMoonPhases))
                ).apply(builder, SpawnRequirements::new)
        );

        public SpawnRequirements(double chance, int minNumberOfNights, Collection<Integer> validMoonPhases) {
            this(chance, minNumberOfNights, new IntArraySet(validMoonPhases));
        }
    }
}
