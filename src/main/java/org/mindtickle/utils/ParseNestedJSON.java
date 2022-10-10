package org.mindtickle.utils;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
public class ParseNestedJSON {
       public static String parseObject(JSONObject json, String key) {
             System.out.println(json.get(key));
             String a =json.get(key).toString();
             return a;
        }
       public static void parseArray(JSONArray jsonArray,String key) {
           try{for(Object jsonObject : jsonArray)
           { parseObject((JSONObject) jsonObject,key);}}catch(Exception e){}

       }

        public static void getKey(JSONObject json, String key) {
            boolean exists = json.has(key);
            Iterator<?> keys;
            String nextKeys;
            if (!exists) {
                keys = json.keys();
                while (keys.hasNext()) {
                    nextKeys = (String) keys.next();
                    try {
                        if (json.get(nextKeys) instanceof JSONObject) {
                            if (exists == false) {
                                getKey(json.getJSONObject(nextKeys), key);
                            }
                        } else if (json.get(nextKeys) instanceof JSONArray) {
                            JSONArray jsonarray = json.getJSONArray(nextKeys);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                String jsonarrayString = jsonarray.get(i).toString();
                                JSONObject innerJSOn = new JSONObject(jsonarrayString);
                                if (exists == false) {
                                    getKey(innerJSOn, key);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            } else {
                parseObject(json, key);
           }
        }



}

