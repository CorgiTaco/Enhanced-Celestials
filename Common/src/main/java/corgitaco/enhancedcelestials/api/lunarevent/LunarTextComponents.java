package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

public record LunarTextComponents(CustomTranslationTextComponent name,
                                  Optional<Notification> riseNotification,
                                  Optional<Notification> setNotification) {


    public static final Codec<LunarTextComponents> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CustomTranslationTextComponent.CODEC.fieldOf("name").forGetter(textComponents -> textComponents.name),
                    Notification.CODEC.optionalFieldOf("startNotification").orElse(Optional.empty()).forGetter(clientSettings -> clientSettings.riseNotification),
                    Notification.CODEC.optionalFieldOf("endNotification").orElse(Optional.empty()).forGetter(clientSettings -> clientSettings.setNotification)
            ).apply(builder, LunarTextComponents::new)
    );

    public LunarTextComponents(CustomTranslationTextComponent name, CustomTranslationTextComponent riseNotification, CustomTranslationTextComponent setNotification) {
        this(name, Optional.of(new Notification(riseNotification, NotificationType.CHAT)), Optional.of(new Notification(setNotification, NotificationType.CHAT)));
    }
    public LunarTextComponents(CustomTranslationTextComponent name, Notification riseNotification, Notification setNotification) {
        this(name, Optional.of(riseNotification), Optional.of(setNotification));
    }

    public record Notification(CustomTranslationTextComponent customTranslationTextComponent,
                               NotificationType notificationType) {
        public static final Codec<Notification> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        CustomTranslationTextComponent.CODEC.fieldOf("component").forGetter(notification -> notification.customTranslationTextComponent),
                        NotificationType.CODEC.fieldOf("type").forGetter(notification -> notification.notificationType)
                ).apply(builder, Notification::new)
        );
    }

    public enum NotificationType implements StringRepresentable {
        CHAT,
        HOT_BAR;

        public static final Codec<NotificationType> CODEC = StringRepresentable.fromEnum(NotificationType::values);

        public static NotificationType byName(String name) {
            return NotificationType.valueOf(name.toUpperCase());
        }

        @Override
        public String getSerializedName() {
            return this.name();
        }
    }
}
