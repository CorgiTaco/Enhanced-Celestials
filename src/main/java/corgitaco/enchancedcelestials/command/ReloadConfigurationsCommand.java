package corgitaco.enchancedcelestials.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.config.EnhancedCelestialsConfig;
import corgitaco.enchancedcelestials.lunarevent.LunarEvent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ReloadConfigurationsCommand {
    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reload").executes((cs) -> reloadConfigurations(cs.getSource()));
    }

    public static int reloadConfigurations(CommandSource source) {
        EnhancedCelestialsConfig.loadConfig(EnhancedCelestials.CONFIG_PATH.resolve(EnhancedCelestials.MOD_ID + "-common.toml"));
        source.sendFeedback(new TranslationTextComponent("commands.reload.success"), true);
        return 1;
    }
}