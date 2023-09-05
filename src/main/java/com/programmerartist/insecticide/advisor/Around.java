package com.programmerartist.insecticide.advisor;

import com.alibaba.fastjson.JSON;
import com.programmerartist.insecticide.conf.IsCut;
import com.programmerartist.insecticide.conf.Setup;
import com.programmerartist.insecticide.tunnel.admin.Container;
import com.programmerartist.insecticide.tunnel.admin.HeartBeats;
import com.programmerartist.insecticide.tunnel.admin.InitSetup;
import com.programmerartist.insecticide.tunnel.pointcut.DomainReport;
import com.programmerartist.insecticide.tunnel.pointcut.Report;
import com.programmerartist.insecticide.util.CommonUtill;
import com.programmerartist.insecticide.util.ExceptionEnum;
import com.programmerartist.insecticide.util.ExceptionUtill;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 环绕切面处理器
 *
 * Created by 程序员Artist on 16/3/22.
 */
public class Around {
    private static final Logger LOGGER = LoggerFactory.getLogger(Around.class);
    private static final String ip     = CommonUtill.getLocalIp();

    /**
     * 构造函数:
     * 必设置属性
     *
     * @param traceName 链路名称,区别不同链路
     * @param didName   业务"用户"id名称
     */
    public Around(String traceName, String didName) throws Exception {
        ExceptionUtill.assertBlankAppend(ExceptionEnum.STARTUP_FAIL_MISS_CONF.getMsg() + " | miss plugin Insecticide conf : traceName, didName ", traceName, didName);

        Container.Local.traceName = traceName;
        Container.Local.didName   = didName;

        try {
            InitSetup.init(Container.Local.getDomainInit());
            IsCut.init = true;

            // 启动心跳
            HeartBeats.initHeatBeats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * // 输出：RecommenderRequest{
     * placementId=0,
     * target=RecommenderTarget{userId=1, location='null', language='null', did='00000001', searchKeyword='null',
     * author='null', hint='null', cityId=1, tagIds=null, brandId=0, newsId=0, keywordsId=0, itemId=0}, params=null,
     * lastId=1458636087418, lastWeight=0.0, exid=0, pageSize=10, forceToExperiment=0, forceNoExperiment=false,
     * debug=false, evictFromLogAnalysis=false, encode=Encode{value=1}, noCache=false, refreshCache=false,
     * leastEffort=false, cookie=null, httpClientInfo=null, predictorCacheTime=21600};
     * getRecommendations;com.xx.dataapp.recsys.recommender.impl.RecommenderServiceImpl@2892dae4;RecommenderResult{
     * items=[], experimentId=0, encode=Encode{value=1}, resultCode=ResultCode{value=0}, requestId=0, rcm='null',
     * debugInfo=DebugInfo{appName='recsys.recommender', latency=-1, messageStack=[]}, lastId=null, lastWeight=0.0,
     * end=false}
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        if(!IsCut.init) {
            LOGGER.info(Setup.LOG_PRE + "未初始化... 切面不处理任何逻辑");
        }

        // 1, 判断白名单,设置链路id
        boolean isWhite = false;
        String[] didInsectTrace = null;
        try {
            if(IsCut.isCut()) {
                Object[] args = pjp.getArgs();

                // 判断是否为白名单did,是的话,返回did和链路id(可能还需要更新)
                didInsectTrace = IsCut.isWhiteAndSetTrace(args);
                if(!ExceptionUtill.containsBlank(didInsectTrace)) isWhite = true;
            }
        } catch (Exception e) {
            LOGGER.error(Setup.LOG_PRE + " error", e);
        }

        // 2, 执行原业务方法
        long start    = System.currentTimeMillis();
        Object result = pjp.proceed();
        long cost     = System.currentTimeMillis() - start;

        if(!isWhite) return result;   // 不用上报,直接返回

        // 3, 发送数据给远程监控server
        try {
            // 准备上报数据
            String tMethod  = Report.getMethod(pjp.getTarget().toString(), pjp.getSignature().getName());
            String jsonRequ = JSON.toJSONString(pjp.getArgs());
            String jsonResp = JSON.toJSONString(result);
            long timestamp  = Long.parseLong(didInsectTrace[1].split("_")[1]);

            DomainReport report = new DomainReport(Container.Local.traceName, didInsectTrace[1],
                    didInsectTrace[0], timestamp, tMethod, jsonRequ, jsonResp, cost, ip);

            // 上报
            Report.report2Server(report);

            LOGGER.debug(Setup.LOG_PRE + "did=" + didInsectTrace[0]);
            LOGGER.debug(Setup.LOG_PRE + "输出method：" + pjp.getSignature().getName() + "\n");
            LOGGER.debug(Setup.LOG_PRE + "输出target：" + pjp.getTarget() + "\n");
            LOGGER.debug(Setup.LOG_PRE + "输出result：" + result + "\n");
        } catch (Exception e) {
            LOGGER.error(Setup.LOG_PRE + " error", e);
        }

        return  result;
    }


    /**
     *
     * @param stop
     */
    public void setStop(boolean stop) throws Exception {
        boolean old = Container.Local.stop;
        Container.setStop(stop);

        // 修改stop
        // InitSetup.init(Container.Local.getDomainInit());

        IsCut.init = true;

        LOGGER.debug(Setup.LOG_PRE + "change param [ stop ], from: " + old + ", to: " + stop);
    }


    /**
     *
     * @param debugName
     */
    public void setDebugName(String debugName) throws Exception {
        Container.LocalDebug.debugName = debugName;
    }

    /**
     *
     * @param debugValue
     */
    public void setDebugValue(Object debugValue) throws Exception {
        Container.LocalDebug.debugValue = debugValue;
    }

}
