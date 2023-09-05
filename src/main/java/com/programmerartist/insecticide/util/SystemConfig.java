package com.programmerartist.insecticide.util;import java.io.IOException;import java.io.InputStream;import java.util.Properties;/** * * * Created by 程序员Artist on 16/2/25. */public class SystemConfig {    private Properties prop;    private String file;    public SystemConfig(String file) {        this.file = file;        init();    }    private void init() {        InputStream is = null;        prop = new Properties();        try {            is = SystemConfig.class.getClassLoader().getResourceAsStream(this.file);            prop.load(is);        } catch (Exception e) {            if (is != null) {                try {                    is.close();                } catch (IOException e1) {                    e1.printStackTrace();                }            }        }    }    public Properties getProp() {        return prop;    }    public String get(String key) {        return this.get(key, null);    }    public String get(String key, String defaultValue) {        if (prop != null) {            return prop.getProperty(key, defaultValue);        }        return null;    }}