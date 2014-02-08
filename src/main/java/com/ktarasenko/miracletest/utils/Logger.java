package com.ktarasenko.miracletest.utils;


import android.util.Log;
import com.ktarasenko.miracletest.BuildConfig;

public class Logger {

    public static void debug(String tag, String message){
        if (BuildConfig.DEBUG)
                Log.d(tag, message);
    }

    public static void debug(String tag, String message, Throwable t){
        if (BuildConfig.DEBUG)
                Log.d(tag, message, t);
    }


    public static void warn(String tag, String message){
        if (BuildConfig.DEBUG)
            Log.w(tag, message);
    }

    public static void warn(String tag, String message, Throwable t){
        if (BuildConfig.DEBUG)
            Log.w(tag, message, t);
    }

    public static void info(String tag, String message){
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    public static void info(String tag, String message, Throwable t){
        if (BuildConfig.DEBUG)
            Log.i(tag, message, t);
    }

    public static void error(String tag, String message){
        if (BuildConfig.DEBUG)
            Log.e(tag, message);
    }

    public static void error(String tag, String message, Throwable t){
        if (BuildConfig.DEBUG)
            Log.e(tag, message, t);
    }



}
