package corgitaco.enchancedcelestials.lunarevent;


import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.data.network.NetworkHandler;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;

public class LunarEventSystem {

    public static final String BLOOD_MOON_EVENT_ID = "BLOOD_MOON";
    public static final String DEFAULT_EVENT_ID = "DEFAULT";
    public static final BloodMoon BLOOD_MOON_EVENT = new BloodMoon();
    public static final Default DEFAULT_EVENT = new Default();

    public static HashMap<String, Double> LUNAR_EVENTS_CONTROLLER = new HashMap<>();

    public static HashMap<String, LunarEvent> LUNAR_EVENTS_MAP = new HashMap<>();

    public static ObjectOpenHashSet<LunarEvent> LUNAR_EVENTS = new ObjectOpenHashSet<>();

    public static void registerDefaultLunarEvents() {
        LUNAR_EVENTS.add(BLOOD_MOON_EVENT);
        LUNAR_EVENTS.add(DEFAULT_EVENT);
    }

    public static void fillLunarEventsMapAndWeatherEventController() {
        for (LunarEvent lunarEvent : LUNAR_EVENTS) {
            LUNAR_EVENTS_MAP.put(lunarEvent.getID(), lunarEvent);
            if (!lunarEvent.getID().equals(DEFAULT_EVENT_ID))
                LUNAR_EVENTS_CONTROLLER.put(lunarEvent.getID(), lunarEvent.getChance());
        }
    }

    private static LunarEvent cachedLunarEvent = DEFAULT_EVENT;

    public static void updateLunarEventPacket(ServerWorld world) {
        long dayTime = world.getWorldInfo().getDayTime();
        if (dayTime >= 13000 && dayTime <= 23500) {
            LunarEvent currentLunarEvent = LUNAR_EVENTS_MAP.get(EnhancedCelestials.getLunarData(world).getEvent());
            if (!cachedLunarEvent.equals(currentLunarEvent)) {
                world.getPlayers().forEach(player -> {
                    NetworkHandler.sendToClient(player, new LunarEventPacket(currentLunarEvent.getID()));
                });
                cachedLunarEvent = currentLunarEvent;
            }
        }
        else {
            LunarEvent currentLunarEvent = LUNAR_EVENTS_MAP.get(EnhancedCelestials.getLunarData(world).getEvent());
            if (currentLunarEvent != DEFAULT_EVENT) {
                EnhancedCelestials.getLunarData(world).setEvent(DEFAULT_EVENT_ID);
                world.getPlayers().forEach(player -> {
                    NetworkHandler.sendToClient(player, new LunarEventPacket(DEFAULT_EVENT_ID));
                });
            }
        }
    }
}
