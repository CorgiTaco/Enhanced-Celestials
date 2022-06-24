package corgitaco.enhancedcelestials.asm;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class EnhancedCelestialsASMMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//        if (targetClassName.equals("net.minecraft.core.RegistryAccess")) {
//            targetClass.methods.forEach(methodNode -> {
//                if (methodNode.name.equals("method_30531")) {
//                    InsnList instructions = methodNode.instructions;
//                    AbstractInsnNode last = instructions.getLast().getPrevious().getPrevious();
//                    instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 0));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/EnhancedCelestialsRegistry", "LUNAR_EVENT_KEY", "Lnet/minecraft/resources/ResourceKey;"));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/lunarevent/LunarEvent", "DIRECT_CODEC", "Lcom/mojang/serialization/Codec;"));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/lunarevent/LunarEvent", "DIRECT_CODEC", "Lcom/mojang/serialization/Codec;"));
//                    instructions.insertBefore(last, new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/core/RegistryAccess", "put", "(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)V", true));
//                    instructions.insertBefore(last, new VarInsnNode(Opcodes.ALOAD, 0));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/EnhancedCelestialsRegistry", "LUNAR_DIMENSION_SETTINGS_KEY", "Lnet/minecraft/resources/ResourceKey;"));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/lunarevent/LunarDimensionSettings", "CODEC", "Lcom/mojang/serialization/Codec;"));
//                    instructions.insertBefore(last, new FieldInsnNode(Opcodes.GETSTATIC, "corgitaco/enhancedcelestials/api/lunarevent/LunarDimensionSettings", "CODEC", "Lcom/mojang/serialization/Codec;"));
//                    instructions.insertBefore(last, new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/core/RegistryAccess", "put", "(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Codec;Lcom/mojang/serialization/Codec;)V", true));
//                }
//            });
//        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
