 package com.es.util;

 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.google.gson.reflect.TypeToken;
 import java.lang.reflect.Type;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import org.apache.commons.lang.StringUtils;










 public class JsonUtil
 {
  private static Gson gson = new Gson();
  private static Gson disableHtmlEscapingGson = (new GsonBuilder()).disableHtmlEscaping().create();

  public static final String EMPTY_JSON_STRING = "[]";

  public static Map<String, String> jsonToStringMap(String json) {
    if (StringUtils.isBlank(json)) {
      return new HashMap<>();
    }

    Type type = (new TypeToken<Map<String, String>>() {  }).getType();
    Map<String, String> result = (Map<String, String>)gson.fromJson(json, type);
    return result;
  }


  public static String mapToJson(Map map) {
    if (map == null || map.isEmpty()) {
      return "[]";
    }
    return gson.toJson(map);
  }

  public static String toJson(Object obj) {
    return gson.toJson(obj);
  }


  public static String mapToJsonDisableHtmlEscaping(Map map) {
    if (map == null || map.isEmpty()) {
      return "[]";
    }
    return disableHtmlEscapingGson.toJson(map);
  }


  public static String objectToJsonDisableHtmlEscaping(Object object) {
    if (object == null) {
      return "[]";
    }
    return disableHtmlEscapingGson.toJson(object);
  }


  public static <T> T fromJson(String json, Class<T> clazz) {
    if (StringUtils.isBlank(json) || "[]".equals(json)) {
      return null;
    }
    return (T)gson.fromJson(jsonStandardizing(json), clazz);
  }


  public static String jsonStandardizing(String json) {
    return json.replace("\n", "").replace("\r", "").replace(" ", "");
  }

  public static String objectToJson(Object object) {
    if (object == null) {
      return "[]";
    }
    return gson.toJson(object);
  }


  public static Object jsonToObject(String json, Class clazz) {
    if (StringUtils.isBlank(json) || "[]".equals(json)) {
      return null;
    }
    return gson.fromJson(json, clazz);
  }

  public static Map<String, Object> jsonToMap(String json) {
    if (StringUtils.isBlank(json) || "[]".equals(json)) {
      return null;
    }
    return (Map<String, Object>)gson.fromJson(json, Map.class);
  }

  public static Map<Integer, Double> jsonToMapIntDouble(String json) {
    if (StringUtils.isBlank(json) || "[]".equals(json)) {
      return null;
    }
    return (Map<Integer, Double>)gson.fromJson(json, Map.class);
  }

  public static <T> List<T> jsonArrayToList(String jsonStr, Class<T> clazz, List<T> results) {
    if (results == null) {
      results = new ArrayList<>();
    }
    List<T> list = (List<T>)gson.fromJson(jsonStr, (new TypeToken<List<T>>() {  }
        ).getType());
    if (list != null) {
      for (T t : list) {
        results.add((T)gson.fromJson(gson.toJson(t), clazz));
      }
    }
    return results;
  }

  public static boolean isEmpty(String jsonStr) {
    return "[]".equals(jsonStr.trim());
  }
}