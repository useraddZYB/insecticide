package com.programmerartist.insecticide.tunnel.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programmerartist.insecticide.conf.Setup;
import com.programmerartist.insecticide.tunnel.admin.constants.DomainHeartBeats;
import com.programmerartist.insecticide.util.ExceptionUtill;
import com.programmerartist.insecticide.util.HttpUtill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public class HeartBeats {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeats.class);

    /**
     * 启动心跳
     */
    public static void initHeatBeats() {
        int tPoolSize = Integer.parseInt(Setup.HeartBeats.THREAD_POOL_SIZE.getValue());
        int delay     = Integer.parseInt(Setup.HeartBeats.INITIAL_DELAY.getValue());
        int period    = Integer.parseInt(Setup.HeartBeats.PERIOD.getValue());

        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(tPoolSize);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    HeartBeats.recieve();
                } catch (Exception e) {
                    LOGGER.error(Setup.LOG_PRE + " error", e);
                }
            }
        }, delay, period, TimeUnit.SECONDS);
    }


    /**
     *
     * 从远程控制器上获取相关 "设置"
     *
     */
    public static void recieve() throws Exception {

        // 调用远程获取返回值
        DomainHeartBeats domain = HeartBeats.beats();

        // 设置值
        if(null!=domain && Container.Local.traceName.equals(domain.getTraceName())) {
            Container.Local.stop = domain.isStop();
            Container.Local.did  = new HashSet<>(domain.getDid());

            LOGGER.debug(Setup.LOG_PRE + "beats: stop=" + domain.isStop() + ", did=" + domain.getDid());
        }
    }


    /**
     *
     * @return
     * @throws Exception
     */
    private static DomainHeartBeats beats() throws Exception {

        String url = Setup.HeartBeats.URL.getValue() + "?traceName=" + Container.Local.traceName;
        String jsonResult = HttpUtill.get(url);

        LOGGER.debug(Setup.LOG_PRE + "beats: url=" + url + ", jsonResult=" + jsonResult);

        return HeartBeats.fromJson(jsonResult);
    }


    /**
     *
     * @param solrResultJson
     * @return
     */
    private static DomainHeartBeats fromJson(String solrResultJson) {
        ExceptionUtill.assertBlank(solrResultJson);

        JSONObject jsonObject = JSON.parseObject(solrResultJson);
        JSONObject domain = jsonObject.getJSONObject("response").getJSONObject("domain");
        if(null == domain) {
            return null;
        }

        return JSONObject.parseObject(domain.toJSONString(), DomainHeartBeats.class);
    }

}
