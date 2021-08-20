package corgitaco.enhancedcelestials.util.loot;

import com.google.gson.*;
import corgitaco.enhancedcelestials.loot.ECNumberProviders;
import net.minecraft.loot.IRandomRange;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.Random;

public class Probability implements IRandomRange {

    final float probability;
    final int resultCount;

    public Probability(float probability, int resultCount) {
        this.probability = probability;
        this.resultCount = resultCount;
    }

    @Override
    public int generateInt(Random rand) {
        return rand.nextFloat() <= this.probability ? this.resultCount : 0;
    }

    @Override
    public ResourceLocation getType() {
        return ECNumberProviders.PROBABILITY;
    }

    public static class Serializer implements JsonDeserializer<Probability>, JsonSerializer<Probability> {
        public JsonElement serialize(Probability probabilityValue, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("probability", probabilityValue.probability);
            jsonObject.addProperty("result_count", probabilityValue.resultCount);
            return jsonObject;
        }

        public Probability deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonElement, "value");
            float probability = JSONUtils.getFloat(jsonobject, "probability");
            int resultCount = JSONUtils.getInt(jsonobject, "result_count");
            return new Probability(probability, resultCount);
        }
    }
}