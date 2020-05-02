package com.github.binarywang.demo.wx.mp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class JsonUtils {
    public static JSONObject toJson(Object obj) {
        return JSON.parseObject(JSONObject.toJSONString(obj));
    }
}
