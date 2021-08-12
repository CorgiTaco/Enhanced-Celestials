package corgitaco.enhancedcelestials;

public class LunarContext {
    public static final String CONFIG_NAME = "lunar-settings.toml";
    private static final String DEFAULT = "enhancedcelestials-none";


//    private final Map<String, LunarEvent> lunarEvents = new HashMap<>();
//    private final ResourceLocation worldID;
//    private final Registry<Biome> biomeRegistry;
//    private final Path lunarConfigPath;
//    private final Path lunarEventsConfigPath;
//    private final File weatherConfigFile;
//
//    private boolean refreshRenderers;
//    private LunarEvent currentEvent;
//
//    //Packet Constructor
//    public LunarContext(String currentEvent, ResourceLocation worldID, Map<String, LunarEvent> lunarEvents) {
//        this(currentEvent, worldID, null, lunarEvents);
//    }
//
//    //Server world constructor
//    public LunarContext(LunarEventSavedData weatherEventSavedData, RegistryKey<World> worldID, Registry<Biome> biomeRegistry) {
//        this(weatherEventSavedData.getEvent(), worldID.getLocation(), biomeRegistry, null);
//    }
//
//    public LunarContext(String currentEvent, ResourceLocation worldID, @Nullable Registry<Biome> biomeRegistry, @Nullable Map<String, LunarEvent> lunarEvents) {
//        this.worldID = worldID;
//        this.biomeRegistry = biomeRegistry;
//        this.lunarConfigPath = Main.CONFIG_PATH.resolve(worldID.getNamespace()).resolve(worldID.getPath()).resolve("lunar");
//        this.lunarEventsConfigPath = this.lunarConfigPath.resolve("events");
//        this.weatherConfigFile = this.lunarConfigPath.resolve(CONFIG_NAME).toFile();
//        boolean isClient = lunarEvents != null;
//        boolean isPacket = biomeRegistry == null;
//    }
}
