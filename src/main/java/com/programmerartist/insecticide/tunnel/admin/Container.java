package com.programmerartist.insecticide.tunnel.admin;


import com.programmerartist.insecticide.tunnel.admin.constants.DomainInit;

import java.util.HashSet;
import java.util.Set;

/**
 * 容器
 *
 * Created by 程序员Artist on 16/3/24.
 */
public class Container {

    /**
     * 本地容器
     */
    public static class Local {
        public static boolean DEFAULT_STOP = false;
        public static boolean stop         = DEFAULT_STOP;      // setStop 可以设置初始值
        public static String traceName     = null;
        public static String didName       = null;

        public static Set<String> did = new HashSet<>();

        /**
         *
         * @return
         */
        public static DomainInit getDomainInit() {
            return new DomainInit(traceName, stop);
        }

    }

    /**
     * 业务方特殊debug逻辑
     *
     */
    public static class LocalDebug {

        public static String debugName  = null;
        public static Object debugValue = null;
    }


    /**
     * 远程控制器server容器
     */
    public static class server {
        public static boolean DEFAULT_STOP = false;
        public static boolean stop         = DEFAULT_STOP;      // setStop 可以设置初始值
    }


    /**
     *
     * @param stop
     * @throws Exception
     */
    public static void setStop(boolean stop) throws Exception {

        Local.stop = stop;
    }


}
