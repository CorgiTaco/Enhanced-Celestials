package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CodecUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;


//TODO: Do we want to use int providers?
public record MobEffectInstanceBuilder(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible,
                                       boolean showIcon) {

    public static final Codec<MobEffectInstanceBuilder> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.MOB_EFFECT.fieldOf("effect").forGetter(MobEffectInstanceBuilder::effect),
                    Codec.INT.fieldOf("duration_in_ticks").forGetter(MobEffectInstanceBuilder::duration),
                    Codec.INT.fieldOf("amplifier").forGetter(MobEffectInstanceBuilder::amplifier),
                    Codec.BOOL.fieldOf("ambient").forGetter(MobEffectInstanceBuilder::ambient),
                    Codec.BOOL.fieldOf("visible").forGetter(MobEffectInstanceBuilder::visible),
                    Codec.BOOL.fieldOf("show_icon").forGetter(MobEffectInstanceBuilder::showIcon)
            ).apply(builder, MobEffectInstanceBuilder::new)
    );


    public MobEffectInstance makeInstance() {
        return new MobEffectInstance(this.effect, this.duration, this.amplifier, this.ambient, this.visible, this.showIcon);
    }
}
