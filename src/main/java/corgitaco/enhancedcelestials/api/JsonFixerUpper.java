package corgitaco.enhancedcelestials.api;

import com.google.gson.JsonElement;

public interface JsonFixerUpper<T> {

    T fixerUpper(JsonElement element);
}
