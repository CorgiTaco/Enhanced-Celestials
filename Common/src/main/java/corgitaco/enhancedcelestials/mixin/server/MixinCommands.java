package corgitaco.enhancedcelestials.mixin.server;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.server.commands.LunarForecastCommand;
import corgitaco.enhancedcelestials.server.commands.SetLunarEventCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addEnhancedCelestialCommands(Commands.CommandSelection $$0, CommandBuildContext $$1, CallbackInfo ci) {
        LiteralArgumentBuilder<CommandSourceStack> requires = Commands.literal(EnhancedCelestials.MOD_ID).requires(commandSource -> commandSource.hasPermission(2));
        requires.then(SetLunarEventCommand.register(dispatcher));
        requires.then(LunarForecastCommand.register(dispatcher));
        dispatcher.register(requires);

        LiteralArgumentBuilder<CommandSourceStack> ecAlias = Commands.literal("ec").requires(commandSource -> commandSource.hasPermission(2));
        ecAlias.then(SetLunarEventCommand.register(dispatcher));
        ecAlias.then(LunarForecastCommand.register(dispatcher));
        dispatcher.register(ecAlias);
        EnhancedCelestials.LOGGER.debug("Registered Enhanced Celestial Commands!");
    }
}