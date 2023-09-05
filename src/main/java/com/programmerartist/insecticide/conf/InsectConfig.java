package com.programmerartist.insecticide.conf;


import com.programmerartist.insecticide.util.SystemConfig;
import org.apache.commons.lang.StringUtils;

/**
 * Created by 程序员Artist on 15/12/31.
 */
public class InsectConfig {

    /**
     * insecticide
     */
    private static SystemConfig innerConfig = new SystemConfig("insecticide.properties");

    /**
     * application
     */
    private static SystemConfig outerConfig = new SystemConfig("insecticide.properties");


    /**
     *
     * @param key
     * @return
     */
    public static String getValue(String key){
        String out = outerConfig.get(key);
        if(StringUtils.isNotBlank(out)) {
            return out;
        }
        return innerConfig.get(key);
    }

}
