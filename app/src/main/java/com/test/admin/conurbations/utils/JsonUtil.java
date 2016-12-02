package com.test.admin.conurbations.utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by waly6 on 2015/10/20.
 */
public class JsonUtil {
    public static <T> T get(Object object, Class<T> cls) {
        JSONObject jsonObject;

        if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String) object);
            }
            catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            jsonObject = (JSONObject) object;
        }

        Gson gson = new Gson();

        try {
            return gson.fromJson(jsonObject.getString("data"), cls);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T jsonToObject(String string, Class<T> cls) {
        return new Gson().fromJson(string, cls);
    }

    public static JSONObject getJSONObject(String string) {
        try {
            return new JSONObject(string);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static <T> String listToJsonArrayString(List<T> list) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (T t : list) {
                jsonArray.put(new JSONObject(new Gson().toJson(t)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }
}

