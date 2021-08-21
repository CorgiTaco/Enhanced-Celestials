package corgitaco.enhancedcelestials.util.loot;

import com.google.gson.*;
import corgitaco.enhancedcelestials.loot.ECNumberProviders;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.Random;

public class Probability implements IRandomRange {
    final float probability;
    private final RandomValueRange randomValueRange;

    public Probability(float probability, RandomValueRange randomValueRange) {
        this.probability = probability;
        this.randomValueRange = randomValueRange;
    }

    @Override
    public int generateInt(Random rand) {
        return rand.nextFloat() <= this.probability ? this.randomValueRange.generateInt(rand) : 0;
    }

    @Override
    public ResourceLocation getType() {
        return ECNumberProviders.PROBABILITY;
    }

    public static class Serializer implements JsonDeserializer<Probability>, JsonSerializer<Probability> {
        public JsonElement serialize(Probability probabilityValue, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("probability", probabilityValue.probability);
            jsonObject.addProperty("min", probabilityValue.randomValueRange.getMin());
            jsonObject.addProperty("max", probabilityValue.randomValueRange.getMax());
            return jsonObject;
        }

        public Probability deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonElement, "value");
            float probability = JSONUtils.getFloat(jsonobject, "probability");
            float min = JSONUtils.getFloat(jsonobject, "min");
            float max = JSONUtils.getFloat(jsonobject, "max");
            RandomValueRange randomValueRange = new RandomValueRange(min, max);

            return new Probability(probability, randomValueRange);
        }
    }
}