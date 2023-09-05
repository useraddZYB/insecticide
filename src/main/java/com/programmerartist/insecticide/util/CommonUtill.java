package com.programmerartist.insecticide.util;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 工具
 *
 * Created by 程序员Artist on 16/2/25.
 */
public class CommonUtill {

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalIp(){

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取本机ip地址后两段
     *
     * @return
     */
    public static String getLocalIpLastTwo(){

        try {
            String ip = getLocalIp();
            if(StringUtils.isNotBlank(ip)) {
                String[] part = ip.split("\\.");
                return part[2]+"."+part[3];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
