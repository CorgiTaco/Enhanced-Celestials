package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.lunarevent.client.MoonClientSettings;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.Util;

import java.util.stream.IntStream;

public class Moon extends LunarEvent {
    public static final Moon MOON = (Moon) new Moon().setKey("moon");

    public static final Codec<Moon> CODEC = Codec.unit(() -> MOON);

    public Moon() {
        super(new MoonClientSettings(new ColorSettings("", 0, "", 0), 20.0F, null), 0, 0, Util.make(new IntArraySet(), (set) -> IntStream.rangeClosed(0, 7).forEach(set::add)),new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.moon"),  CustomTranslationTextComponent.DEFAULT, CustomTranslationTextComponent.DEFAULT), false);
    }



    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
