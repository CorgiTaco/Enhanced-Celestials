package corgitaco.enhancedcelestials.mixin.access;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemTags.class)
public interface ItemTagsAccess {

    @Invoker
    static Tag.Named<Item> invokeBind(String string) {
        throw new Error("Mixin did not apply!");
    }
}
