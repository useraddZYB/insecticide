package com.programmerartist.insecticide.tunnel.pointcut;

import com.alibaba.fastjson.JSON;

/**
 * 上报给服务端的数据
 *
 * Created by 程序员Artist on 16/3/24.
 */
public class DomainReport {

    private String traceName;   // 链路业务名称
    private String traceId;     // 每一个请求链路的唯一id
    private String did;         // 业务"用户"id
    private long timestamp;     // 调用时候打的时间戳
    private String method;      // 方法名称
    private String jsonRequ;    // 方法参数json格式
    private String jsonResp;    // 方法响应json格式
    private long cost;          // 方法耗时
    private String ip;

    public DomainReport() {
    }

    /**
     *
     * @param traceName
     * @param traceId
     * @param did
     * @param timestamp
     * @param method
     * @param jsonRequ
     * @param jsonResp
     * @param cost
     * @param ip
     */
    public DomainReport(String traceName, String traceId, String did, long timestamp, String method, String jsonRequ, String jsonResp, long cost, String ip) {
        this.traceName = traceName;
        this.traceId = traceId;
        this.did = did;
        this.timestamp = timestamp;
        this.method = method;
        this.jsonRequ = jsonRequ;
        this.jsonResp = jsonResp;
        this.cost = cost;
        this.ip = ip;
    }

    public String getTraceName() {
        return traceName;
    }

    public void setTraceName(String traceName) {
        this.traceName = traceName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getJsonRequ() {
        return jsonRequ;
    }

    public void setJsonRequ(String jsonRequ) {
        this.jsonRequ = jsonRequ;
    }

    public String getJsonResp() {
        return jsonResp;
    }

    public void setJsonResp(String jsonResp) {
        this.jsonResp = jsonResp;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DomainReport{" +
                "traceName='" + traceName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", did='" + did + '\'' +
                ", timestamp=" + timestamp +
                ", method='" + method + '\'' +
                ", jsonRequ='" + jsonRequ + '\'' +
                ", jsonResp='" + jsonResp + '\'' +
                ", cost=" + cost +
                ", ip='" + ip + '\'' +
                '}';
    }

    /**
     *
     * @return
     */
    public String toJson() {

        return JSON.toJSONString(this);
    }
}
