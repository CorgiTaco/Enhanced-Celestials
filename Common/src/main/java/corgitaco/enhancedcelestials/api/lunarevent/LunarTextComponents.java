package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;

public class LunarTextComponents {

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
        }), Codec.either(CustomTranslationTextComponent.CODEC, Notification.CODEC).xmap(e -> e.map((textComponent) -> new Notification(textComponent, NotificationType.CHAT), notification -> notification), Either::right).optionalFieldOf("startNotification", Notification.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.riseNotification;
        }), Codec.either(CustomTranslationTextComponent.CODEC, Notification.CODEC).xmap(e -> e.map((textComponent) -> new Notification(textComponent, NotificationType.CHAT), notification -> notification), Either::right).optionalFieldOf("endNotification", Notification.DEFAULT).forGetter((clientSettings) -> {
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

    public static class Notification {
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
    }

    public enum NotificationType implements StringRepresentable {
        CHAT,
        HOT_BAR;

        public static final Codec<NotificationType> CODEC = StringRepresentable.fromEnum(NotificationType::values, NotificationType::byName);

        public static NotificationType byName(String name) {
            return NotificationType.valueOf(name.toUpperCase());
        }

        @Override
        public String getSerializedName() {
            return this.name();
        }
    }
}
