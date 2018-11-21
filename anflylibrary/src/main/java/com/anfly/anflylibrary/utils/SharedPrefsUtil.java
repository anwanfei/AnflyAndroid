package com.anfly.anflylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.yunji.delivery.DeliApplication;
import ai.yunji.delivery.bean.CruiseJsonPost;
import ai.yunji.running.entity.Marker;

public class SharedPrefsUtil {
    public final static String SETTING = "db_haidilao";
    private static Gson gson = new Gson();


    public static void put(String key, int value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static void put(String key, long value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public static void put(String key, boolean value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static void put(String key, String value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static int get(String key, int defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean get(String key, boolean defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static void remove(String key) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().remove(key).commit();
    }


    public static void saveDistrictMap(HashMap<String, ArrayList<String>> data) {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String jsonStr = gson.toJson(data); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("districtMap", jsonStr); //存入json串
        editor.commit();  //提交
    }

    public static HashMap<String, ArrayList<String>> getDistrictMap() {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String str = sp.getString("districtMap", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (!Kits.Empty.check(str)) {
            HashMap<String, ArrayList<String>> districtMap = gson.fromJson(str, new TypeToken<HashMap<String, ArrayList<String>>>() {
            }.getType()); //将json字符串转换成List集合
            return districtMap;
        }
        return null;
    }


    public static void saveMenu(ArrayList<String> data) {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String jsonStr = gson.toJson(data); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("menulist", jsonStr); //存入json串
        editor.commit();  //提交
    }

    public static ArrayList<String> getMenu() {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String str = sp.getString("menulist", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (!Kits.Empty.check(str)) {
            Gson gson = new Gson();
            ArrayList<String> taskList = gson.fromJson(str, new TypeToken<ArrayList<String>>() {
            }.getType()); //将json字符串转换成List集合
            return taskList;
        }
        return null;
    }


    public static void savePointList(List<CruiseJsonPost.DataBean.TargetsBean> data, List<Marker> markers) {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String jsonStr = gson.toJson(data); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("point_list", jsonStr);
        editor.putString("marker_list", gson.toJson(markers));//存入json串
        editor.commit();  //提交
    }


    public static ArrayList<CruiseJsonPost.DataBean.TargetsBean> getPointList() {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String str = sp.getString("point_list", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (str != "") {
            ArrayList<CruiseJsonPost.DataBean.TargetsBean> pointList = gson.fromJson(str, new TypeToken<ArrayList<CruiseJsonPost.DataBean.TargetsBean>>() {
            }.getType()); //将json字符串转换成List集合
            return pointList;
        }
        return null;
    }

    public static ArrayList<Marker> getMarkerList() {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String str = sp.getString("marker_list", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (str != "") {
            ArrayList<Marker> markers = gson.fromJson(str, new TypeToken<ArrayList<Marker>>() {
            }.getType()); //将json字符串转换成List集合
            return markers;
        }
        return null;
    }


}
