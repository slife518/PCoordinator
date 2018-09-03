package com.company.jk.pcoordinator.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jungkukjo on 31/08/2018.
 */

public class JsonParse {


    //jsonArray형식의 stringbuffer 에서 필요한 정보만 jsonobject 에 담에서 리턴
    public static JSONObject getJsonObjectFromStringBuffer(StringBuffer sb, String group) {

        JSONObject jsonObject = new JSONObject(); //group들로 구성된 json
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();

        try {

            jsonObject = new JSONObject(sb.toString());
            jsonArray = new JSONArray(jsonObject.getString(group));  //
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }

    public static JSONObject getJsonObjectFromString(String rs, String group) {

        JSONObject jsonObject = new JSONObject(); //group들로 구성된 json
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject = new JSONObject(rs);
            jsonArray = new JSONArray(jsonObject.getString(group));  //
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject1 = (JSONObject) jsonArray.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }

    //StringBuffer 에서 jsonObject 추출
    public static JSONObject getJsonObjectFromStringBuffer(StringBuffer sb){
        JSONObject jsonObject = new JSONObject(); //group들로 구성된 json

        try {
            jsonObject = new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    //StringBuffer 에서 JSONArray
    public static JSONArray getJsonArrayFromStringBuffer(StringBuffer sb){
        JSONArray jsonArray = new JSONArray(); //group들로 구성된 json

        try {
            jsonArray = new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonArray;
    }

    //StringBuffer 에서 JSONArray
    public static JSONArray getJsonArrayFromString(String rs){
        JSONArray jsonArray = new JSONArray(); //group들로 구성된 json

        try {
            jsonArray = new JSONArray(rs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonArray;
    }


    public static JSONArray getJsonArrayFromString(String rs, String group) {

        JSONObject jsonObject = new JSONObject(); //group들로 구성된 json
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject = new JSONObject(rs);
            jsonArray = new JSONArray(jsonObject.getString(group));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    //result 이름의 jsonObject 를 jsonArray 에 담는다.
    public static  JSONArray getJsonArrayFromJsonObject(JSONObject jsonObject, String name){
        JSONArray jsonArray = new JSONArray();
        try{
            jsonArray = jsonObject.getJSONArray(name);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return  jsonArray;
    }

    public  static JSONObject getJsonObjectFromMap(Map<String, Object> map){
        JSONObject jsonObject = new JSONObject(); //group들로 구성된 json
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  jsonObject;
    }
}
