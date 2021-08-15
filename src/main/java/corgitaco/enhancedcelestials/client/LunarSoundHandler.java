package corgitaco.enhancedcelestials.client;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BiomeSoundHandler;
import net.minecraft.client.audio.IAmbientSoundHandler;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.SoundEvent;

public class LunarSoundHandler implements IAmbientSoundHandler {

    private final ObjectOpenHashSet<BiomeSoundHandler.Sound> activeLunarSoundsMap = new ObjectOpenHashSet<>();
    private final SoundHandler soundHandler;
    private final ClientWorld world;
    private LunarEvent lunarEvent;

    public LunarSoundHandler(ClientWorld world) {
        this.world = world;
        this.soundHandler = Minecraft.getInstance().getSoundHandler();
    }

    @Override
    public void tick() {
        this.activeLunarSoundsMap.removeIf(TickableSound::isDonePlaying);
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();

        if (lunarContext == null) {
            return;
        }

        LunarEvent currentEvent = lunarContext.getCurrentEvent();
        SoundEvent soundTrack = currentEvent.getClient().getSoundTrack(); // Use client directly here.
        if (currentEvent != this.lunarEvent || this.activeLunarSoundsMap.isEmpty()) {
            this.lunarEvent = currentEvent;
            this.activeLunarSoundsMap.forEach(BiomeSoundHandler.Sound::fadeOutSound);
            if (soundTrack != null) {
                BiomeSoundHandler.Sound sound = new BiomeSoundHandler.Sound(soundTrack);
                this.activeLunarSoundsMap.add(sound);
                this.soundHandler.play(sound);
                sound.fadeInSound();
            }
        }
    }
}
