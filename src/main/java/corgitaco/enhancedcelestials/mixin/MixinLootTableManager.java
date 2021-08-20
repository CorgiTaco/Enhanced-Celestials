package corgitaco.enhancedcelestials.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import corgitaco.enhancedcelestials.mixin.access.JsonReloadListenerAccess;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.loot.LootTableManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Mixin(LootTableManager.class)
public abstract class MixinLootTableManager extends JsonReloadListener {

    public MixinLootTableManager(Gson gson, String string) {
        super(gson, string);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "lambda$apply$0(Lnet/minecraft/resources/IResourceManager;Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;)V", at = @At("HEAD"), remap = false)
    private void appendTables(IResourceManager resourceManagerIn, ImmutableMap.Builder builder, ResourceLocation location, JsonElement element, CallbackInfo ci) {
        appendTable(resourceManagerIn, location, element);
    }

    private void appendTable(IResourceManager resourceManager, ResourceLocation id, JsonElement element) {
        for (String resourceNamespace : resourceManager.getResourceNamespaces()) {
            try (IResource appendedTable = resourceManager.getResource(getAppendedPath(resourceNamespace, id))) {
                InputStream inputstream = appendedTable.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                JsonElement appendedElement = JSONUtils.fromJson(((JsonReloadListenerAccess) this).getGson(), reader, JsonElement.class);
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
