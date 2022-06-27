package corgitaco.enhancedcelestials.save;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;

public class LunarForecastSavedData extends SavedData {
    public static final String DATA_NAME = EnhancedCelestials.MOD_ID + "_lunar_event_data";

    private static LunarForecastSavedData clientCache = new LunarForecastSavedData();
    private static ClientLevel worldCache = null;


    public static LunarForecastSavedData get(LevelAccessor world) {
        if (!(world instanceof ServerLevel)) {
            if (worldCache != world) {
                worldCache = (ClientLevel) world;
                clientCache = new LunarForecastSavedData();
            }
            return clientCache;
        }
        DimensionDataStorage data = ((ServerLevel) world).getDataStorage();
        LunarForecastSavedData lunarData = data.computeIfAbsent(LunarForecastSavedData::load, LunarForecastSavedData::new, DATA_NAME);
        if (lunarData == null) {
            lunarData = new LunarForecastSavedData();
            data.set(DATA_NAME, lunarData);
        }
        return lunarData;
    }

    @Nullable
    private LunarForecast.Data forecastData;

    public LunarForecastSavedData(LunarForecast.Data lunarForecast) {
        this.forecastData = lunarForecast;
    }

    public LunarForecastSavedData() {
        this(null);
    }

    public static LunarForecastSavedData load(CompoundTag nbt) {
        return new LunarForecastSavedData(LunarForecast.Data.CODEC.decode(NbtOps.INSTANCE, nbt.get("forecast")).result().orElseThrow().getFirst());
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("forecast", LunarForecast.Data.CODEC.encodeStart(NbtOps.INSTANCE, forecastData).result().orElseThrow());
        return compound;
    }

    @Nullable
    public LunarForecast.Data getForecastSaveData() {
        return forecastData;
    }

    public void setForecastSaveData(LunarForecast.Data forecastData) {
        this.forecastData = forecastData;
        setDirty();
    }
}
