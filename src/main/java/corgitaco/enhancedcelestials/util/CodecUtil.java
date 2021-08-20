package corgitaco.enhancedcelestials.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.mixin.access.ColorAccess;
import corgitaco.enhancedcelestials.mixin.access.StyleAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;

import java.util.Optional;
import java.util.function.Supplier;

public class CodecUtil {

    public static final Codec<ClickEvent.Action> CLICK_EVENT_ACTION_CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(ClickEvent.Action.valueOf(s));
        } catch (Exception e) {
            Main.LOGGER.error(e.getMessage());
            return DataResult.error(e.getMessage());
        }
    }, ClickEvent.Action::name);


    public static final Codec<ClickEvent> CLICK_EVENT_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(CLICK_EVENT_ACTION_CODEC.fieldOf("action").forGetter((clickEvent) -> {
            return clickEvent.getAction();
        }), Codec.STRING.fieldOf("value").forGetter((clickEvent -> {
            return clickEvent.getValue();
        })) ).apply(builder, ClickEvent::new);
    });

    public static final Codec<Style> STYLE_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.STRING.optionalFieldOf("color", "").forGetter((style) -> {
            return style.getColor() != null ? Integer.toHexString(style.getColor().getValue()) : Integer.toHexString(ChatFormatting.WHITE.getColor());
        }), Codec.BOOL.optionalFieldOf("bold", false).forGetter((style) -> {
            return style.isBold();
        }), Codec.BOOL.optionalFieldOf("italic", false).forGetter((style) -> {
            return style.isItalic();
        }), Codec.BOOL.optionalFieldOf("underlined", false).forGetter((style) -> {
            return style.isUnderlined();
        }), Codec.BOOL.optionalFieldOf("strikethrough", false).forGetter((style) -> {
            return style.isStrikethrough();
        }), Codec.BOOL.optionalFieldOf("obfuscated", false).forGetter((style) -> {
            return style.isObfuscated();
        }), CLICK_EVENT_CODEC.optionalFieldOf("clickEvent").forGetter((style) -> {
            return Optional.of(style.getClickEvent());
        } )).apply(builder, (color, bold, italic, underlined, strikethrough, obfuscated, clickEvent) -> StyleAccess.create(ColorAccess.create(ColorSettings.tryParseColor(color)), bold, italic, underlined, strikethrough, obfuscated, clickEvent.orElse(null), null, null, null));
    });



    public static class LazyCodec<TYPE> implements Codec<TYPE> {
        private final Supplier<Codec<TYPE>> delegate;

        public LazyCodec(Supplier<Codec<TYPE>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public <T> DataResult<T> encode(TYPE input, DynamicOps<T> ops, T prefix) {
            return this.delegate().get().encode(input, ops, prefix);
        }

        @Override
        public <T> DataResult<Pair<TYPE, T>> decode(DynamicOps<T> ops, T input) {
            return this.delegate().get().decode(ops, input);
        }

        public Supplier<Codec<TYPE>> delegate() {
            return this.delegate;
        }
    }
}
