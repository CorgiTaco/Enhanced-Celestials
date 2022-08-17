package corgitaco.enhancedcelestials.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.mixin.access.ColorAccess;
import corgitaco.enhancedcelestials.mixin.access.StyleAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;
import java.util.function.Supplier;

public class CodecUtil {

    public static final Codec<MobEffect> MOB_EFFECT = createLoggedExceptionRegistryCodec(Registry.MOB_EFFECT);
    public static final Codec<EntityType<?>> ENTITY_TYPE = createLoggedExceptionRegistryCodec(Registry.ENTITY_TYPE);



    public static final Codec<ClickEvent.Action> CLICK_EVENT_ACTION_CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(ClickEvent.Action.valueOf(s));
        } catch (Exception e) {
            EnhancedCelestials.LOGGER.error(e.getMessage());
            return DataResult.error(e.getMessage());
        }
    }, ClickEvent.Action::name);


    public static final Codec<ClickEvent> CLICK_EVENT_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                CLICK_EVENT_ACTION_CODEC.fieldOf("action").forGetter(ClickEvent::getAction),
                Codec.STRING.fieldOf("value").forGetter((ClickEvent::getValue))
        ).apply(builder, ClickEvent::new);
    });

    public static final Codec<Style> STYLE_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                Codec.STRING.optionalFieldOf("color", "").forGetter((style) -> style.getColor() != null ? Integer.toHexString(((ColorAccess) (Object) style.getColor()).getColorRaw()) : Integer.toHexString(ChatFormatting.WHITE.getColor())), Codec.BOOL.optionalFieldOf("bold", false).forGetter(Style::isBold),
                Codec.BOOL.optionalFieldOf("italic", false).forGetter(Style::isItalic),
                Codec.BOOL.optionalFieldOf("underlined", false).forGetter(Style::isUnderlined),
                Codec.BOOL.optionalFieldOf("strikethrough", false).forGetter(Style::isStrikethrough),
                Codec.BOOL.optionalFieldOf("obfuscated", false).forGetter(Style::isObfuscated),
                CLICK_EVENT_CODEC.optionalFieldOf("click_event").forGetter((style) -> style.getClickEvent() != null ? Optional.of(style.getClickEvent()) : Optional.empty())
        ).apply(builder, (color, bold, italic, underlined, strikethrough, obfuscated, clickEvent) -> StyleAccess.create(ColorAccess.create(ColorSettings.tryParseColor(color)), bold, italic, underlined, strikethrough, obfuscated, clickEvent.orElse(null), null, null, null));
    });


    public record LazyCodec<TYPE>(Supplier<Codec<TYPE>> delegate) implements Codec<TYPE> {

        @Override
        public <T> DataResult<T> encode(TYPE input, DynamicOps<T> ops, T prefix) {
            return this.delegate().get().encode(input, ops, prefix);
        }

        @Override
        public <T> DataResult<Pair<TYPE, T>> decode(DynamicOps<T> ops, T input) {
            return this.delegate().get().decode(ops, input);
        }
    }

    public static <T> Codec<T> createLoggedExceptionRegistryCodec(Registry<T> registry) {
        return ResourceLocation.CODEC.comapFlatMap(location -> {
            final Optional<T> result = registry.getOptional(location);

            if (result.isEmpty()) {
                StringBuilder registryElements = new StringBuilder();
                for (int i = 0; i < registry.entrySet().size(); i++) {
                    final T object = registry.byId(i);
                    registryElements.append(i).append(". \"").append(registry.getKey(object).toString()).append("\"\n");
                }

                return DataResult.error(String.format("\"%s\" is not a valid entityType in registry: %s.\nCurrent Registry Values:\n\n%s\n", location.toString(), registry.toString(), registryElements.toString()));
            }
            return DataResult.success(result.get());
        }, registry::getKey);
    }
}
