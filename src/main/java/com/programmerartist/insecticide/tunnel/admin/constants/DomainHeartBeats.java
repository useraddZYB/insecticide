package com.programmerartist.insecticide.tunnel.admin.constants;

import java.util.List;

/**
 * Created by 程序员Artist on 16/3/24.
 */
public class DomainHeartBeats {

    private String traceName;   // 链路业务名称
    private List<String> did;   // 业务"用户"id
    private boolean stop;       // 是否关闭上报

    /**
     * json用
     */
    public DomainHeartBeats() {
    }

    /**
     *
     * @param traceName
     * @param did
     * @param stop
     */
    public DomainHeartBeats(String traceName, List<String> did, boolean stop) {
        this.traceName = traceName;
        this.did = did;
        this.stop = stop;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public List<String> getDid() {
        return did;
    }

    public void setDid(List<String> did) {
        this.did = did;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }


    @Override
    public String toString() {
        return "DomainHeartBeats{" +
                "traceName='" + traceName + '\'' +
                ", did=" + did +
                ", stop=" + stop +
                '}';
    }
}
