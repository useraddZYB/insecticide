package com.programmerartist.insecticide.conf;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public interface Setup {

    String LOG_PRE      = "[insecticide] ";
    String INSECT_TRACE = "insectTrace";    // 前段标示唯一链路, 后端为了排序用: DateUtill.getTimestampRandom() + "_" + DateUtill.getTimestamp();
    String DNS          = "http://" + InsectConfig.getValue("insect.dns");


    /**
     * 心跳参数设置
     */
    public enum HeartBeats {
        THREAD_POOL_SIZE(InsectConfig.getValue("insect.heart.tpool.size")),
        INITIAL_DELAY(InsectConfig.getValue("insect.heart.initialDelay")),
        PERIOD(InsectConfig.getValue("insect.heart.period")),
        URL(DNS + InsectConfig.getValue("insect.heart.url"));

        private String value;

        HeartBeats(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 初始化参数设置
     */
    public enum Init {
        URL(DNS + InsectConfig.getValue("insect.init.url"));

        private String value;

        Init(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 上报服务参数设置
     */
    public enum Report {
        URL(DNS + InsectConfig.getValue("insect.report.url")),
        THREAD_POOL_CORE(InsectConfig.getValue("insect.report.tPool.core")),
        THREAD_POOL_MAX(InsectConfig.getValue("insect.report.tPool.max")),
        THREAD_POOL_KEEP_TIME(InsectConfig.getValue("insect.report.tPool.keepTime"));

        private String value;

        Report(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }



}
