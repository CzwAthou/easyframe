///*
// * Copyright (c) 2016  athou（cai353974361@163.com）.
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.athou.frame.util;
//
//import android.content.Context;
//import android.content.res.AssetManager;
//import android.content.res.Resources;
//import android.support.v4.content.ContextCompat;
//
//import junit.framework.Assert;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//import rx.Observable;
//import rx.functions.Func1;
//
//import static android.content.res.Resources.getSystem;
//
///**
// * Created by athou on 2016/9/30.
// */
//
//public class PropertiesUtil {
//
//    public static Properties loadProperties(String propertiesName){
//        InputStream inputStream =null;
//        try {
//            inputStream = Resources.getSystem().getAssets().open(propertiesName);
//            Properties properties = new Properties();
//            properties.load(inputStream);
//            return properties;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
//
//    public static Properties readInt(Properties properties, String[] key){
//            Observable.from(key)
//                    .map(new Func1<String, String>() {
//                        @Override
//                        public String call(String s) {
//                            loadProperties("").getProperty()
//                            return null;
//                        }
//                    })
//
//    }
//
//    public static Properties readLong(Properties properties){
//        properties.getProperty()
//        Properties properties = loadProperties(propertiesName);
//    }
//
//    public static Properties readFloat(Properties properties){
//        properties.getProperty()
//        Properties properties = loadProperties(propertiesName);
//    }
//
//    public static Properties readString(Properties properties){
//        properties.getProperty()
//        Properties properties = loadProperties(propertiesName);
//    }
//
//    public void saveConfig(String file, Properties properties) {
//        try {
//            FileOutputStream s = new FileOutputStream(file, false);
//            properties.store(s, "");
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
