package com.programmerartist.insecticide.tunnel.pointcut;

import com.alibaba.fastjson.JSONObject;
import com.programmerartist.insecticide.conf.Setup;
import com.programmerartist.insecticide.util.HttpUtill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 上报服务
 *
 * Created by 程序员Artist on 16/3/24.
 */
public class Report {
    private static final Logger LOGGER = LoggerFactory.getLogger(Report.class);

    private static ExecutorService tPool  = new ThreadPoolExecutor
            (
            Integer.parseInt(Setup.Report.THREAD_POOL_CORE.getValue()),
            Integer.parseInt(Setup.Report.THREAD_POOL_MAX.getValue()),
            Long.parseLong(Setup.Report.THREAD_POOL_KEEP_TIME.getValue()),
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    /**
     * 上报数据给server
     *
     * @param dReport
     * @throws Exception
     */
    public static void report2Server(final DomainReport dReport) throws Exception {

        tPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String url = Setup.Report.URL.getValue();
                    JSONObject json = new JSONObject();
                    json.put("domain", dReport);
                    String jsonStr = json.toJSONString();
                    HttpUtill.post(url, jsonStr);

                    LOGGER.debug(Setup.LOG_PRE + "report2Server: url=" + url + ", json=" + jsonStr);
                } catch (Exception e) {
                    LOGGER.error(Setup.LOG_PRE + " error", e);
                }
            }
        });
    }

    /**
     *
     * @param targetObj
     * @param method
     * @return
     */
    public static String getMethod(String targetObj, String method) {

        return targetObj.substring(targetObj.lastIndexOf(".") + 1, targetObj.lastIndexOf("@")) + "." + method + "()";
    }


}
