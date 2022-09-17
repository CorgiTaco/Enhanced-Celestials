package corgitaco.enhancedcelestials.core;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.entity.MeteorEntity;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import corgitaco.enhancedcelestials.reg.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ECEntities {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registry.ENTITY_TYPE, EnhancedCelestials.MOD_ID);

    public static final RegistryObject<EntityType<MeteorEntity>> METEOR = createEntity("meteor", EntityType.Builder.<MeteorEntity>of(MeteorEntity::new, MobCategory.MISC).sized(3, 3).clientTrackingRange(512).updateInterval(Integer.MAX_VALUE));

    public static <E extends Entity> RegistryObject<EntityType<E>> createEntity(String id, EntityType.Builder<E> entityType) {
        return ENTITIES.register(id, () -> entityType.build(EnhancedCelestials.MOD_ID + ":" + id));
    }
}
