package corgitaco.enchancedcelestials.data.world;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class LunarData extends WorldSavedData {
    public static String DATA_NAME = EnhancedCelestials.MOD_ID + ":lunar_data";

    private String event = LunarEventSystem.DEFAULT_EVENT_ID;

    public LunarData() {
        super(DATA_NAME);
    }

    public LunarData(String s) {
        super(s);
    }

    @Override
    public void read(CompoundNBT nbt) {
        setEvent(nbt.getString("event"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("event", event);
        return compound;
    }

    public String getEvent() {
        return this.event;
    }


    public void setEvent(String event) {
        this.event = event;
        markDirty();
        EnhancedCelestials.currentLunarEvent = LunarEventSystem.LUNAR_EVENTS_MAP.get(event);
    }


    public static LunarData get(IWorld world) {
        if (!(world instanceof ServerWorld))
            return new LunarData();
        ServerWorld overWorld = ((ServerWorld) world).getWorld().getServer().getWorld(World.OVERWORLD);
        DimensionSavedDataManager data = overWorld.getSavedData();
        LunarData weatherData = data.getOrCreate(LunarData::new, DATA_NAME);

        if (weatherData == null) {
            weatherData = new LunarData();
            data.set(weatherData);
        }

        return weatherData;
    }
}