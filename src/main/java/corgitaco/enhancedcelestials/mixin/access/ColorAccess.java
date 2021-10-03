package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextColor.class)
public interface ColorAccess {

    @Invoker("<init>")
    static TextColor create(int hex) {
        throw new Error("Mixin did not apply");
    }

    @Accessor("value")
    int getColorRaw();
}
