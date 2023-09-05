package com.programmerartist.insecticide.conf;

import com.programmerartist.insecticide.tunnel.admin.Container;
import com.programmerartist.insecticide.util.ExceptionUtill;
import com.programmerartist.insecticide.util.ReflectUtill;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 切面开关器
 *
 * Created by 程序员Artist on 16/3/24.
 */
public class IsCut {
    public static volatile boolean init = false;

    /**
     *
     * @return
     */
    public static boolean isCut() {

        return init && !Container.Local.stop;
    }

    /**
     * 是白名单,才要上报
     *
     * @return null ? 非白名单用户 : 是白名单且返回[did, insectTrace]
     */
    public static String[] isWhiteAndSetTrace(Object[] args) throws Exception {
        if(ExceptionUtill.containsBlank(args)) return null;

        for(Object arg : args) {
            String did = ReflectUtill.drillDeep(arg, Container.Local.didName);

            if(StringUtils.isNotBlank(did) && Container.Local.did.contains(did)) { //

                String[] didInsectTrace = new String[2];
                String newInsectTrace = "";
                // 如果没有insectTrace链路id, 则说明是链路第一步, 需要手动设置一个
                String insectTrace = ReflectUtill.drill(arg, Setup.INSECT_TRACE);
                if(ExceptionUtill.containsBlank(insectTrace)) {
                    newInsectTrace = randomInsectTrace();

                }
                // 如果有此id, 则说明是上一层传过来的, 需要拿到且更新_后面的时间戳(用作server端汇总排序)
                else {
                    newInsectTrace = updateInsectTrace(insectTrace);
                }

                ReflectUtill.setField(arg, Setup.INSECT_TRACE, newInsectTrace);
                if(!ExceptionUtill.containsBlank(Container.LocalDebug.debugName, Container.LocalDebug.debugValue)) {
                    ReflectUtill.setField(arg, Container.LocalDebug.debugName, Container.LocalDebug.debugValue);
                }

                didInsectTrace[0] = did;
                didInsectTrace[1] = newInsectTrace;

                return didInsectTrace;
            }
        }

        // 没有此did属性,或者did不是白名单监控用户,则返回null
        return null;
    }

    /**
     *
     * @return
     */
    private static String randomInsectTrace() {

        return getTimestampRandom() + "_" + System.currentTimeMillis();
    }

    /**
     *
     * @param insectTrace
     * @return
     */
    private static String updateInsectTrace(String insectTrace) {

        return insectTrace.split("_")[0] + "_" + System.currentTimeMillis();
    }

    /**
     *
     * @return
     */
    private static String getTimestampRandom() {

        String four = ThreadLocalRandom.current().nextInt(9999) + "";
        int miss = 4 - four.length();
        String missStr = "";
        for(int i=0; i<miss; i++) missStr += "0";

        return System.currentTimeMillis() + missStr + four;
    }

}
