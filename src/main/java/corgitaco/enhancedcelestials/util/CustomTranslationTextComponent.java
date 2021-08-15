package corgitaco.enhancedcelestials.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomTranslationTextComponent extends TranslationTextComponent {

    public static final CustomTranslationTextComponent DEFAULT = new CustomTranslationTextComponent("", Style.EMPTY);

    public static final Codec<CustomTranslationTextComponent> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.STRING.fieldOf("key").forGetter((customTranslationTextComponent -> {
            return customTranslationTextComponent.getKey();
        })), CodecUtil.STYLE_CODEC.optionalFieldOf("style", Style.EMPTY).forGetter((customTranslationTextComponent -> {
            return customTranslationTextComponent.getStyle();
        })), new CodecUtil.LazyCodec<>(() -> CustomTranslationTextComponent.CODEC).listOf().optionalFieldOf("args", new ArrayList<>()).forGetter((customTranslationTextComponent -> {
            return customTranslationTextComponent.args;
        }))).apply(builder, CustomTranslationTextComponent::new);
    });

    private final List<CustomTranslationTextComponent> args;

    public CustomTranslationTextComponent(String translationKey, CustomTranslationTextComponent... args) {
        this(translationKey, Style.EMPTY, args);
    }

    public CustomTranslationTextComponent(String translationKey, Style style, CustomTranslationTextComponent... args) {
        super(translationKey, args);
        this.setStyle(style);
        this.args = Arrays.asList(args);
    }

    public CustomTranslationTextComponent(String translationKey, Style style, List<CustomTranslationTextComponent> args) {
        super(translationKey, args.toArray());
        this.args = args;
        this.setStyle(style);
    }
}
