package com.programmerartist.insecticide.advisor;

import com.programmerartist.insecticide.tunnel.admin.Container;
import com.programmerartist.insecticide.tunnel.pointcut.DomainReport;

import java.lang.reflect.Field;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public class Test {

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception{

        // 输出target：com.xx.dataapp.recsys.recommender.impl.RecommenderServiceImpl@26275bef

        String method = "getRecommendations";
        String obj = "com.xx.dataapp.recsys.recommender.impl.RecommenderServiceImpl@26275bef";

        System.out.println(obj.substring(obj.lastIndexOf(".")+1, obj.lastIndexOf("@")) + "." + method + "()");


        //Container.Local.did

        DomainReport domainReport = new DomainReport();
        Container.LocalDebug.debugName  = "cost";
        Container.LocalDebug.debugValue = "15";

        Class clazz = domainReport.getClass();
        Field field = clazz.getDeclaredField("cost");

    }

}
