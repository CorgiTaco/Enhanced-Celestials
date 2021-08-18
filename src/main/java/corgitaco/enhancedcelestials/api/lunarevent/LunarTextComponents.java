package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;

import javax.annotation.Nullable;

public class LunarTextComponents {

    public static final Codec<LunarTextComponents> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(CustomTranslationTextComponent.CODEC.fieldOf("name").forGetter((textComponents) -> {
            return textComponents.name;
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("startNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.riseNotification;
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("endNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.setNotification;
        })).apply(builder, LunarTextComponents::new);
    });

    private final CustomTranslationTextComponent name;
    @Nullable
    private final CustomTranslationTextComponent riseNotification;
    @Nullable
    private final CustomTranslationTextComponent setNotification;

    public LunarTextComponents(CustomTranslationTextComponent name, CustomTranslationTextComponent riseNotification, CustomTranslationTextComponent setNotification) {
        this.name = name;
        this.riseNotification = riseNotification == CustomTranslationTextComponent.DEFAULT || riseNotification.getKey().isEmpty() ? null : riseNotification;
        this.setNotification = setNotification == CustomTranslationTextComponent.DEFAULT || setNotification.getKey().isEmpty() ? null : setNotification;
    }

    public CustomTranslationTextComponent getName() {
        return name;
    }

    @Nullable
    public CustomTranslationTextComponent getRiseNotification() {
        return riseNotification;
    }

    @Nullable
    public CustomTranslationTextComponent getSetNotification() {
        return setNotification;
    }
}
