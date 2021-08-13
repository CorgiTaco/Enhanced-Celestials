package corgitaco.enhancedcelestials.save;

import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.Main;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;

public class LunarEventSavedData extends WorldSavedData {
    public static final String DATA_NAME = new ResourceLocation(Main.MOD_ID, "lunar_event_data").toString();

    private static LunarEventSavedData clientCache = new LunarEventSavedData();
    private static ClientWorld worldCache = null;

    public static LunarEventSavedData get(IWorld world) {
        if (!(world instanceof ServerWorld)) {
            if (worldCache != world) {
                worldCache = (ClientWorld) world;
                clientCache = new LunarEventSavedData();
            }
            return clientCache;
        }
        DimensionSavedDataManager data = ((ServerWorld) world).getSavedData();
        LunarEventSavedData weatherData = data.getOrCreate(LunarEventSavedData::new, DATA_NAME);

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
    public void read(CompoundNBT nbt) {
        forecast = LunarForecast.CODEC.decode(NBTDynamicOps.INSTANCE, nbt.get("forecast")).result().get().getFirst();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("forecast", LunarForecast.CODEC.encodeStart(NBTDynamicOps.INSTANCE, forecast).result().get());
        return compound;
    }

    @Nullable
    public LunarForecast getForecast() {
        return forecast;
    }

    public void setForecast(LunarForecast forecast) {
        this.forecast = forecast;
        markDirty();
    }
}
