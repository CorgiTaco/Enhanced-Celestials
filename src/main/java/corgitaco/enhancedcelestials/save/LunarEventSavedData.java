package corgitaco.enhancedcelestials.save;

import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.Main;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;

public class LunarEventSavedData extends SavedData {
    public static final String DATA_NAME = new ResourceLocation(Main.MOD_ID, "lunar_event_data").toString();

    private static LunarEventSavedData clientCache = new LunarEventSavedData();
    private static ClientLevel worldCache = null;

    public static LunarEventSavedData get(LevelAccessor world) {
        if (!(world instanceof ServerLevel)) {
            if (worldCache != world) {
                worldCache = (ClientLevel) world;
                clientCache = new LunarEventSavedData();
            }
            return clientCache;
        }
        DimensionDataStorage data = ((ServerLevel) world).getDataStorage();
        LunarEventSavedData weatherData = data.computeIfAbsent(LunarEventSavedData::new, DATA_NAME);

        if (weatherData == null) {
            weatherData = new LunarEventSavedData();
            data.set(weatherData);
        }
        return weatherData;
    }

    @Nullable
    private LunarForecast forecast;

    public LunarEventSavedData(String name) {
        super(name);
    }

    public LunarEventSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void load(CompoundTag nbt) {
        forecast = LunarForecast.CODEC.decode(NbtOps.INSTANCE, nbt.get("forecast")).result().get().getFirst();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("forecast", LunarForecast.CODEC.encodeStart(NbtOps.INSTANCE, forecast).result().get());
        return compound;
    }

    @Nullable
    public LunarForecast getForecast() {
        return forecast;
    }

    public void setForecast(LunarForecast forecast) {
        this.forecast = forecast;
        setDirty();
    }
}
