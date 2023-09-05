package com.programmerartist.insecticide.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * httpclient工具集
 *
 * Created by yideng on 16/2/27.
 */
public class HttpUtill {

    private static final int TYPE_GET  = 0;
    private static final int TYPE_POST = 1;

    /**
     * 工具方法：请求url, 返回json字符串
     *
     * @param url 有参数,直接追加在url后面即可,按照http协议来
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {

        return requestUrl(url, TYPE_GET, null);
    }


    /**
     * 工具方法：请求url, 返回json字符串
     *
     * @param url
     * @param jsonParam 没有,传null
     * @return
     * @throws Exception
     */
    public static String post(String url, String jsonParam) throws Exception {

        return requestUrl(url, TYPE_POST, jsonParam);
    }


    /**
     *  保留所有键盘可见字符；其他如非法符号、表情符等全部过滤掉
     *
     * @param originString
     * @return
     */
    public static String filterSolr(String originString){
        if(StringUtils.isNotBlank(originString)){
            return originString.replaceAll("[^ ~`!！@#$%^&*（）()-_—+=\\[\\]【】{}\\|:：;；'\"“”<>《》.。,，?/a-zA-Z0-9\u4e00-\u9fa5]", "");
        }
        return originString;
    }




    //============================================tools============================================================

    /**
     *
     * @param url
     * @param type
     * @param jsonParam
     * @return
     * @throws Exception
     */
    private static String requestUrl(String url, int type, String jsonParam) throws Exception {
        if(StringUtils.isBlank(url)) return "";

        String jsonResult = "";
        CloseableHttpResponse response = null;

        try{
            // prepare and excute
            CloseableHttpClient client = HttpClients.createDefault();
            if(TYPE_GET == type){
                HttpGet method = new HttpGet(url);

                response = client.execute(method);
            }else if(TYPE_POST == type){
                HttpPost method = new HttpPost(url);
                if(StringUtils.isNotBlank(jsonParam)) {
                    StringEntity entity = new StringEntity(jsonParam, "utf-8"); //解决中文乱码问题
                    // entity.setContentEncoding("UTF-8");        // 解决服务端500报错
                    entity.setContentType("application/json");
                    method.setEntity(entity);
                }

                response = client.execute(method);
            }

            // response
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                HttpEntity entity = response.getEntity();
                jsonResult = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
            }else{
                /**
                 * reason=Internal Server Error
                 * jsonResult=unsupported content-encoding: Content-Encoding: UTF-8
                 */
                System.out.println("reason=" + response.getStatusLine().getReasonPhrase());
                HttpEntity entity = response.getEntity();
                jsonResult = EntityUtils.toString(entity, "UTF-8");
                System.out.println("jsonResult=" + jsonResult);

                throw new Exception("http request failed, URL="+url+", jsonParam="+jsonParam+", type="+type+", CODE="+response.getStatusLine().getStatusCode());
            }
        }finally {
            if(null != response) response.close();
        }

        return jsonResult;
    }


    //============================================ main eg: ============================================================

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // String detail = HttpUtill.get("https://api.beyou.im/v1/291439347290/star/" + 1);

        System.out.println(filterSolr("#aaaa@"));

        /**

         JSONObject jsonObj = JSONObject.parseObject(detail);
         int status = jsonObj.getInteger("status");

         // 失败，则打印日志，返回空的map
         if(status == 0) {
         logger.error("task fortune detail error, code="+jsonObj.getString("code")+", error="+jsonObj.getString("error"));
         return id2Detail;
         }

         // 成功
         if(status == 1) {
            JSONObject data = jsonObj.getJSONObject("data");


         }

         */
    }


}
