package corgitaco.enhancedcelestials.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import corgitaco.enhancedcelestials.mixin.access.JsonReloadListenerAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Mixin(LootTables.class)
public abstract class MixinLootTableManager extends SimpleJsonResourceReloadListener {

    @Shadow @Final private static Gson GSON;

    public MixinLootTableManager(Gson gson, String string) {
        super(gson, string);
    }

    private static final ThreadLocal<ResourceManager> RESOURCE_MANAGER = new ThreadLocal<>();

    @Inject(method = "apply", at = @At(value = "HEAD"))
    private void attachResourceManager(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        RESOURCE_MANAGER.set(resourceManager);
    }

    @Inject(method = "apply", at = @At("RETURN"))
    private void detachResourceManager(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        RESOURCE_MANAGER.set(null);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$apply$0(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonElement;)V", at = @At("HEAD"))
    private static void appendTables(ImmutableMap.Builder builder, ResourceLocation location, JsonElement element, CallbackInfo ci) {
        appendTable(RESOURCE_MANAGER.get(), location, element);
    }

    private static void appendTable(ResourceManager resourceManager, ResourceLocation id, JsonElement element) {
        for (String resourceNamespace : resourceManager.getNamespaces()) {
            try (Resource appendedTable = resourceManager.getResource(getAppendedPath(resourceNamespace, id))) {
                InputStream inputstream = appendedTable.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                JsonElement appendedElement = GsonHelper.fromJson(GSON, reader, JsonElement.class);
                JsonArray pools = element.getAsJsonObject().getAsJsonArray("pools");
                pools.addAll(appendedElement.getAsJsonObject().getAsJsonArray("pools"));
            } catch (IOException e) {

            }
        }
    }

    private static ResourceLocation getAppendedPath(String s, ResourceLocation resourceLocation) {
        return new ResourceLocation(s, "append_loot_tables/" + resourceLocation.toString().replace(":", "/") + ".json");
    }
}
