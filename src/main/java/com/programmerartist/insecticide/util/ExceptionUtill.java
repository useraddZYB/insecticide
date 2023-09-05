package com.programmerartist.insecticide.util;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 异常工具类
 *
 * Created by 程序员Artist on 16/3/22.
 */
public class ExceptionUtill {

    /**
     * 断言空参数, 有空参数直接报错
     *
     * @param params
     */
    public static void assertBlank(Object... params) {
        if(null==params || params.length==0) throwError(ExceptionEnum.ILLEGAL_ARGUMENT, "Object... params is null");

        for(Object param : params) {
            if(null==param
                    || (param instanceof String && (param).equals(""))
                    || (param instanceof Collection && ((Collection) param).size() == 0)
                    || (param instanceof Map && ((Map) param).size() == 0)
                    || (param.getClass().isArray() && ((Object[]) param).length == 0)) {

                throwError(ExceptionEnum.ILL_ARG_BLANk, logBlank(params));
            }
        }
    }

    /**
     * 断言null参数, 有null参数直接报错
     *
     * @param params
     */
    public static void assertNull(Object... params) {
        if(null==params || params.length==0) throwError(ExceptionEnum.ILLEGAL_ARGUMENT, "Object... params is null");

        for(Object param : params) {
            if(null == param) {
                throwError(ExceptionEnum.ILL_ARG_NULL, ExceptionUtill.logBlank(params));
            }
        }
    }

    /**
     * 断言空参数, 有空参数直接报错
     *
     * @param append 异常详细里追加此参数
     * @param params
     */
    public static void assertBlankAppend(String append, Object... params) {

        if(ExceptionUtill.containsBlank(params)) {
            throwError(ExceptionEnum.ILL_ARG_BLANk, append + " | " + logBlank(params));
        }
    }

    /**
     * 断言null参数, 有null参数直接报错
     *
     * @param append 异常详细里追加此参数
     * @param params
     */
    public static void assertNullAppend(String append, Object... params) {

        if(ExceptionUtill.containsNull(params)) {
            throwError(ExceptionEnum.ILL_ARG_BLANk, append + " | " + logBlank(params));
        }
    }

    /**
     * 断言空参数, 有空参数 return true
     *
     * @param params
     */
    public static boolean containsBlank(Object... params) {
        if(null==params || params.length==0) return true;

        for(Object param : params) {
            if(null==param
                    || (param instanceof String && (param).equals(""))
                    || (param instanceof Collection && ((Collection) param).size() == 0)
                    || (param instanceof Map && ((Map) param).size() == 0)
                    || (param.getClass().isArray() && ((Object[]) param).length == 0)) {

                return true;
            }
        }

        return false;
    }

    /**
     * 断言null参数, 有null参数 return true
     *
     * @param params
     */
    public static boolean containsNull(Object... params) {
        if(null==params || params.length==0) return true;

        for(Object param : params) {
            if(null == param) {
                return true;
            }
        }

        return false;
    }

    /**
     * 抛出数据应用组相关异常
     *
     * @param recEnum
     */
    public static void throwError(ExceptionEnum recEnum) {
        throwError(recEnum, null);
    }

    /**
     * 抛出数据应用组相关异常
     *
     * @param recEnum 异常详细里追加此参数
     * @param append
     */
    public static void throwError(ExceptionEnum recEnum, String append) {
        if(null == recEnum) return ;

        if(StringUtils.isBlank(append)) {
            throw new RuntimeException(recEnum.getValue());
        }else {
            throw new RuntimeException(recEnum.getValue() + "; detail info is = ( " + append + " )");
        }
    }


    /**
     * 打印参数是否为空的概要,空的话[], 不空就是[..]
     *
     * @param params
     * @return
     */
    public static String logBlank(Object... params) {
        if(null==params || params.length==0) throwError(ExceptionEnum.ILLEGAL_ARGUMENT, "Object... params is null");

        StringBuilder sb = new StringBuilder();
        for(Object param : params) {
            if(null == param) {
                sb.append("null, ");
            }else {
                if(param instanceof String){
                    if(param.equals("")){
                        sb.append("\"\", ");
                    }else{
                        sb.append("String, ");
                    }
                }else if(param instanceof Collection){
                    if(((Collection) param).size() == 0){
                        sb.append("Collection[], ");
                    }else{
                        sb.append("Collection[..], ");
                    }
                }else if(param instanceof Map){
                    if(((Map) param).size() == 0){
                        sb.append("Map[], ");
                    }else{
                        sb.append("Map[..], ");
                    }
                }else if(param.getClass().isArray()){
                    if(((Object[]) param).length == 0){
                        sb.append("Array[], ");
                    }else{
                        sb.append("Array[..], ");
                    }
                }else {
                    sb.append("Object, ");
                }
            }
        }
        String sbStr = sb.toString().trim();
        if(sbStr.length() > 0) sbStr = sbStr.substring(0, sbStr.length()-1);

        return sbStr;
    }


}
