package corgitaco.enhancedcelestials.client;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import corgitaco.enhancedcelestials.lunarevent.LunarForecast;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;

public class LunarSoundHandler implements AmbientSoundHandler {

    private final ObjectOpenHashSet<BiomeAmbientSoundsHandler.LoopSoundInstance> activeLunarSoundsMap = new ObjectOpenHashSet<>();
    private final SoundManager soundHandler;
    private final ClientLevel world;
    private LunarEvent lunarEvent;

    public LunarSoundHandler(ClientLevel world) {
        this.world = world;
        this.soundHandler = Minecraft.getInstance().getSoundManager();
    }

    @Override
    public void tick() {
        this.activeLunarSoundsMap.removeIf(AbstractTickableSoundInstance::isStopped);
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (enhancedCelestialsContext == null) {
            this.activeLunarSoundsMap.forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::fadeOut);
            return;
        }

        LunarForecast lunarForecast = enhancedCelestialsContext.getLunarForecast();
        LunarEvent currentEvent = lunarForecast.getCurrentEvent(world.getRainLevel(1) < 1).value();
        SoundEvent soundTrack = currentEvent.getClientSettings().soundTrack(); // Use client directly here.
        if (currentEvent != this.lunarEvent || this.activeLunarSoundsMap.isEmpty()) {
            this.lunarEvent = currentEvent;
            this.activeLunarSoundsMap.forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::fadeOut);
            if (soundTrack != null) {
                BiomeAmbientSoundsHandler.LoopSoundInstance sound = new BiomeAmbientSoundsHandler.LoopSoundInstance(soundTrack);
                this.activeLunarSoundsMap.add(sound);
                this.soundHandler.play(sound);
                sound.fadeIn();
            }
        }
    }
}
