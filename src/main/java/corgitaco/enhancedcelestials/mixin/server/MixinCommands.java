package corgitaco.enhancedcelestials.mixin.server;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.server.commands.SetLunarEventCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class MixinCommands {

    @Shadow
    @Final
    private CommandDispatcher<CommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addEnhancedCelestialCommands(Commands.EnvironmentType envType, CallbackInfo ci) {
        LiteralCommandNode<CommandSource> requires = dispatcher.register(Commands.literal(Main.MOD_ID).requires(commandSource -> commandSource.hasPermissionLevel(3)).then(SetLunarEventCommand.register(dispatcher)));
        dispatcher.register(Commands.literal("ec").redirect(requires).requires((source) -> source.hasPermissionLevel(3))); // Create 'ec' alias.
        Main.LOGGER.debug("Registered Enhanced Celestial Commands!");
    }
}