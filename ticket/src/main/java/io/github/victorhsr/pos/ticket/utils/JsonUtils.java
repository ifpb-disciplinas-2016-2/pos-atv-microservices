/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.ticket.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static JsonElement getJsonElementFromString(String json, String atributo) {;
        return getJsonObjectFromString(json).get(atributo);
    }

    public static JsonObject getJsonObjectFromString(String json) {
        return GSON.fromJson(json, JsonObject.class);
    }

}
