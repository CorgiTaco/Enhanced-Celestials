package corgitaco.enchancedcelestials;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.data.network.NetworkHandler;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import corgitaco.enchancedcelestials.data.world.LunarData;
import corgitaco.enchancedcelestials.lunarevent.LunarEvent;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import corgitaco.enchancedcelestials.modcompat.OptifineCompat;
import corgitaco.enchancedcelestials.server.SetLunarEventCommand;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod(EnhancedCelestials.MOD_ID)
public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials"; //Enhanced Celestials/Celestial Enhancement
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Path CONFIG_PATH = new File(String.valueOf(FMLPaths.CONFIGDIR.get().resolve(MOD_ID))).toPath();

    public static LunarData lunarData = null;
    public static LunarEvent currentLunarEvent = null;
    public static boolean usingOptifine;

    public EnhancedCelestials() {
        CONFIG_PATH.toFile().mkdirs();

        EnhancedCelestialsConfig.loadConfig(CONFIG_PATH.resolve(MOD_ID + "-common.toml"));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::lateSetup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.init();
        LunarEventSystem.registerDefaultLunarEvents();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        usingOptifine = OptifineCompat.IS_OPTIFINE_PRESENT.getValue();
    }

    private void lateSetup(FMLLoadCompleteEvent event) {
        currentLunarEvent = LunarEventSystem.DEFAULT_EVENT;
        LunarEventSystem.fillLunarEventsMapAndWeatherEventController();
    }

    public static LunarData getLunarData(@Nullable IWorld world) {
        if (lunarData == null) {
            if (world == null) {
                throw new NullPointerException("Attempting to set Lunar data w/ a null world!");
            } else {
                lunarData = LunarData.get(world);
            }
        }
        return lunarData;
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EnhancedCelestialsEvents {
        @SubscribeEvent
        public static void worldTick(TickEvent.WorldTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                if (event.side.isServer()) {
                    ServerWorld serverWorld = (ServerWorld) event.world;
                    if (EnhancedCelestialsUtils.isOverworld(serverWorld.getDimensionKey())) {
                        World world = event.world;
                        if (EnhancedCelestialsUtils.modulosDaytime(world.getWorldInfo().getDayTime()) == 12000) {
                            AtomicBoolean lunarEventWasSet = new AtomicBoolean(false);
                            LunarEventSystem.LUNAR_EVENTS_CONTROLLER.forEach((eventName, eventChance) -> {
                                if (world.rand.nextDouble() < eventChance) {
                                    lunarEventWasSet.set(true);
                                    getLunarData(event.world).setEvent(eventName);
                                }
                            });
                            if (!lunarEventWasSet.get()) {
                                getLunarData(event.world).setEvent(LunarEventSystem.DEFAULT_EVENT.getID());
                            }

                            ((ServerWorld) event.world).getPlayers().forEach(player -> {
                                NetworkHandler.sendToClient(player, new LunarEventPacket(getLunarData(event.world).getEvent()));
                            });
                        }
                        LunarEventSystem.updateLunarEventPacket(serverWorld);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
            updateLunarEventPacket(Collections.singletonList((ServerPlayerEntity) event.getPlayer()));
        }

        public static void updateLunarEventPacket(List<ServerPlayerEntity> players) {
            players.forEach(player -> {
                NetworkHandler.sendToClient(player, new LunarEventPacket(getLunarData(player.getServerWorld()).getEvent()));
            });
        }

        @SubscribeEvent
        public static void commandRegisterEvent(FMLServerStartingEvent event) {
            LOGGER.debug("Enhanced Celestials: \"Server Starting\" Event Starting...");
            register(event.getServer().getCommandManager().getDispatcher());
            LOGGER.info("Enhanced Celestials: \"Server Starting\" Event Complete!");
        }
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LOGGER.debug("Registering Enhanced Celestials commands...");
        LiteralArgumentBuilder<CommandSource> requires = Commands.literal(MOD_ID).requires(commandSource -> commandSource.hasPermissionLevel(3));
        LiteralCommandNode<CommandSource> source = dispatcher.register(requires.then(SetLunarEventCommand.register(dispatcher)));
        dispatcher.register(Commands.literal(MOD_ID).redirect(source));
        LOGGER.debug("Registered Enhanced Celestials Commands!");
    }
}