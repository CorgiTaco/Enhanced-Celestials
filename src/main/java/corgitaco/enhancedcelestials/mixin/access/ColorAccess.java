package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.util.text.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Color.class)
public interface ColorAccess {

    @Invoker("<init>")
    static Color create(int hex) {
        throw new Error("Mixin did not apply");
    }
}
