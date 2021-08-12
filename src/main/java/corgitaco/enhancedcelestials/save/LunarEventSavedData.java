package corgitaco.enhancedcelestials.save;

import corgitaco.enhancedcelestials.Main;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

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


    public LunarEventSavedData(String name) {
        super(name);
    }

    public LunarEventSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return null;
    }
}
