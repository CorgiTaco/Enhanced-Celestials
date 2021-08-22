package corgitaco.enhancedcelestials.api.lunarevent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.JsonFixerUpper;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

public class LunarTextComponents implements JsonFixerUpper<LunarTextComponents> {

    public static final Codec<LunarTextComponents> LEGACY_CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(CustomTranslationTextComponent.CODEC.fieldOf("name").forGetter((textComponents) -> {
            return textComponents.name;
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("startNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.riseNotification.getCustomTranslationTextComponent();
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("endNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.setNotification.getCustomTranslationTextComponent();
        })).apply(builder, LunarTextComponents::new);
    });

    public static final Codec<LunarTextComponents> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(CustomTranslationTextComponent.CODEC.fieldOf("name").forGetter((textComponents) -> {
            return textComponents.name;
        }), Notification.CODEC.optionalFieldOf("startNotification", Notification.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.riseNotification;
        }), Notification.CODEC.optionalFieldOf("endNotification", Notification.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.setNotification;
        })).apply(builder, LunarTextComponents::new);
    });

    private final CustomTranslationTextComponent name;
    @Nullable
    private final Notification riseNotification;
    @Nullable
    private final Notification setNotification;

    public LunarTextComponents(CustomTranslationTextComponent name, CustomTranslationTextComponent startNotificationComponent, CustomTranslationTextComponent endNotificationComponent) {
        this(name, new Notification(startNotificationComponent, NotificationType.CHAT), new Notification(endNotificationComponent, NotificationType.CHAT));
    }

    public LunarTextComponents(CustomTranslationTextComponent name, Notification riseNotification, Notification setNotification) {
        this.name = name;
        this.riseNotification = riseNotification == Notification.DEFAULT || riseNotification.customTranslationTextComponent.getKey().isEmpty() ? null : riseNotification;
        this.setNotification = setNotification == Notification.DEFAULT || setNotification.customTranslationTextComponent.getKey().isEmpty() ? null : setNotification;
    }

    public CustomTranslationTextComponent getName() {
        return name;
    }

    @Nullable
    public Notification getRiseNotification() {
        return riseNotification;
    }

    @Nullable
    public Notification getSetNotification() {
        return setNotification;
    }

    @Override
    public LunarTextComponents fixerUpper(JsonElement element) {
        return new LunarTextComponents(name == null ? CustomTranslationTextComponent.DEFAULT : name, riseNotification != null ? riseNotification.fixerUpper(element) : createNotificationFixer(element, "startNotification"), setNotification != null ? setNotification.fixerUpper(element) : createNotificationFixer(element, "endNotification"));
    }

    public static Notification createNotificationFixer(JsonElement element, String... textComponentKeys) {
        JsonObject asJsonObject = element.getAsJsonObject();
        CustomTranslationTextComponent textComponent = null;
        for (String textComponentKey : textComponentKeys) {
            if (asJsonObject.has(textComponentKey)) {
                textComponent = CustomTranslationTextComponent.CODEC.decode(JsonOps.INSTANCE, asJsonObject.get(textComponentKey)).get().left().get().getFirst();
            }
        }
        return new Notification(textComponent == null ? CustomTranslationTextComponent.DEFAULT : textComponent, NotificationType.CHAT);
    }

    public static class Notification implements JsonFixerUpper<Notification> {
        public static final Notification DEFAULT = new Notification(CustomTranslationTextComponent.DEFAULT, NotificationType.CHAT);
        public static final Codec<Notification> CODEC = RecordCodecBuilder.create((builder) -> {
            return builder.group(CustomTranslationTextComponent.CODEC.fieldOf("component").forGetter((notification) -> {
                return notification.customTranslationTextComponent;
            }), NotificationType.CODEC.fieldOf("type").forGetter((notification) -> {
                return notification.notificationType;
            })).apply(builder, Notification::new);
        });
        private final CustomTranslationTextComponent customTranslationTextComponent;
        private final NotificationType notificationType;

        public Notification(CustomTranslationTextComponent customTranslationTextComponent, NotificationType notificationType) {
            this.customTranslationTextComponent = customTranslationTextComponent;
            this.notificationType = notificationType;
        }

        public CustomTranslationTextComponent getCustomTranslationTextComponent() {
            return customTranslationTextComponent;
        }

        public NotificationType getNotificationType() {
            return notificationType;
        }

        @Override
        public Notification fixerUpper(JsonElement element) {
            return new Notification(customTranslationTextComponent == null ? CustomTranslationTextComponent.DEFAULT : customTranslationTextComponent, this.notificationType == null ? NotificationType.CHAT : notificationType);
        }
    }

    public enum NotificationType implements IStringSerializable {
        CHAT,
        HOT_BAR;

        public static final Codec<NotificationType> CODEC = IStringSerializable.fromEnum(NotificationType::values, NotificationType::byName);

        public static NotificationType byName(String name) {
            return NotificationType.valueOf(name.toUpperCase());
        }

        @Override
        public String getSerializedName() {
            return this.name();
        }
    }
}
