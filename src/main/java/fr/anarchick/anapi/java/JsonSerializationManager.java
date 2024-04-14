package fr.anarchick.anapi.java;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

@SuppressWarnings("unused")
public class JsonSerializationManager<T> {

    private final Gson gson;

    public JsonSerializationManager() {
        this.gson = createGsonInstance();
    }

    private Gson createGsonInstance() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    public String serialize(T obj) {
        return this.gson.toJson(obj);
    }

    public T deserialization(String json, Class<T> clazz) {
        return this.gson.fromJson(json, clazz);
    }
    
    public Gson get() {
    	return this.gson;
    }
    
    public Map<?, ?> getMap(String json) {
    	return this.gson.fromJson(json, Map.class);
    }
    
}
