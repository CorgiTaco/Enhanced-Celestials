package corgitaco.enhancedcelestials.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import corgitaco.enhancedcelestials.Main;
import corgitaco.enhancedcelestials.mixin.access.JsonReloadListenerAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Mixin(LootTables.class)
public abstract class MixinLootTableManager extends SimpleJsonResourceReloadListener {

    public MixinLootTableManager(Gson gson, String string) {
        super(gson, string);
    }

    @Inject(method = "apply", at = @At("HEAD"))
    private void appendTables(Map<ResourceLocation, JsonElement> values, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        String appendTablesDir = "append_loot_tables";
        Collection<ResourceLocation> resourceLocations = resourceManager.listResources(appendTablesDir, (key) -> key.endsWith(".json"));
        for (ResourceLocation resourceLocation : resourceLocations) {
            ResourceLocation key = new ResourceLocation(resourceLocation.getPath().replace(appendTablesDir + "/", "").replaceFirst("/", ":").replace(".json", ""));
            if (values.containsKey(key)) {
                values.get(key).getAsJsonObject().getAsJsonArray("pools").addAll(extractPools(resourceManager, resourceLocation));
            }
        }
    }

    private JsonArray extractPools(ResourceManager resourceManager, ResourceLocation location) {
        try (Resource appendedTable = resourceManager.getResource(location)) {
            InputStream inputstream = appendedTable.getInputStream();
            Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
            JsonElement appendedElement = GsonHelper.fromJson(((JsonReloadListenerAccess) this).getGson(), reader, JsonElement.class);
            return appendedElement.getAsJsonObject().getAsJsonArray("pools");
        } catch (IOException e) {
            Main.LOGGER.error("Could not read appended table:" + location.toString());
            e.printStackTrace();
        }
        return new JsonArray();
    }
}
