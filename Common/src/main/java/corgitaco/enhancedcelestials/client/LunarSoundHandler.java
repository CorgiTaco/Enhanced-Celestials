package corgitaco.enhancedcelestials.client;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

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

        Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(this.world);
        if (lunarForecastWorldData.isEmpty()) {
            this.activeLunarSoundsMap.forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::fadeOut);
            return;
        }

        EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();

        LunarEvent currentEvent = data.currentLunarEvent();
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
