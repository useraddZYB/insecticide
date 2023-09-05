package com.programmerartist.insecticide.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 高级工具类:
 * 代码分析相关
 *
 *
 * Created by 程序员Artist on 16/3/22.
 */
public class FindUtill {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindUtill.class);

    /**
     *
     * @param obj2String
     * @return
     */
    public static FieldNode getFieldTree(String fieldName, String obj2String) {
        ExceptionUtill.assertBlank(fieldName, obj2String);

        // 不存在此属性,就直接返回
        int index = paramIndex(fieldName, obj2String);
        if(index == -1) return null;

        // 存在的话,继续判断,此属性是否只有唯一的一个,有多个则直接报错(如果有多个重名的属性,则可能取错属性)
        if(!onlyOne(fieldName, obj2String)) ExceptionUtill.throwError(ExceptionEnum.UNSUP_DOUBT, fieldName + " is not only one");

        FieldNode node   = new FieldNode(fieldName, null, null, true);
        String objStrNew = obj2String.substring(0, index);
        LOGGER.debug("[fieldTree] 1=" + objStrNew);

        fieldTree(node, objStrNew, false, 0);

        LOGGER.debug("[fieldTree] node=" + node);
        return node;
    }


    /**
     *
     * @param obj
     * @return
     */
    public static FieldNode getFieldTree(String fieldName, Object obj) {
        return getFieldTree(fieldName, obj.toString());
    }


    /**
     *
     * @param fieldName
     * @param obj
     * @return
     */
    public static boolean containsField(String fieldName, Object obj) {
        return containsField(fieldName, obj.toString());
    }

    /**
     *
     * @param fieldName
     * @param obj2String
     * @return
     */
    public static boolean containsField(String fieldName, String obj2String) {
        ExceptionUtill.assertBlank(fieldName, obj2String);

        if(-1 != paramIndex(fieldName, obj2String)) {
            return true;
        }

        return false;
    }




//====================================tools======================================================


    /**
     *
     * @param fieldName
     * @param obj2String
     * @return , 或者 { 后面1位
     */
    private static int paramIndex(String fieldName, String obj2String) {
        if(-1==obj2String.indexOf("{") || -1==obj2String.indexOf("=")) {
            // 20160602 为了更好的"容忍性",如果参数没有标准的toString方法,则直接返回不包含
            //ExceptionUtill.throwError(ExceptionEnum.UNSUP_VIOLATE_PROMISE, obj2String + " : no standard toString method");

            return -1;
        }

        String wrapperHeader = "{" + fieldName + "=";
        String wrapperBody   = ", " + fieldName + "=";

        int indexHeader = obj2String.indexOf(wrapperHeader);
        if(indexHeader != -1) return indexHeader + 1;

        int indexBody   = obj2String.indexOf(wrapperBody);
        if(indexBody != -1) return indexBody + 1;

        return -1;
    }

    /**
     *  对象obj2String里是否只含有一个名叫fieldName的属性
     *
     * @param fieldName
     * @param obj2String
     * @return
     */
    private static boolean onlyOne(String fieldName, String obj2String) {
        int indexHeader = -1;
        int indexBody   = -1;

        String wrapperHeader = "{" + fieldName + "=";
        String wrapperBody   = ", " + fieldName + "=";

        int headerLen = wrapperHeader.length();
        int bodyLen   = wrapperBody.length();

        indexHeader = obj2String.indexOf(wrapperHeader);
        if(indexHeader != -1) {
            obj2String = obj2String.substring(0, indexHeader) + obj2String.substring(indexHeader + headerLen, obj2String.length());//obj2String.replaceFirst(wrapperHeader, "");

            indexHeader = obj2String.indexOf(wrapperHeader);
            if(indexHeader != -1) return false;

            indexBody = obj2String.indexOf(wrapperBody);
            if(indexBody != -1) return false;
        }

        indexBody = obj2String.indexOf(wrapperBody);
        if(indexBody != -1) {
            obj2String = obj2String.substring(0, indexBody) + obj2String.substring(indexBody + bodyLen, obj2String.length()); //obj2String.replaceFirst(wrapperBody, "");

            indexBody = obj2String.indexOf(wrapperBody);
            if(indexBody != -1) return false;
        }

        return true;
    }


    /**
     *
     * @param node
     * @param obj2String
     * @param end
     * @param safe
     */
    private static void fieldTree(FieldNode node, String obj2String, boolean end, Integer safe) {
        if(StringUtils.isBlank(obj2String) || end) return ;
        String debugPre = "[fieldTree] ";
        // 防止死循环保护
        safe += 1;
        LOGGER.debug(debugPre + "safe=" + safe);
        if(safe >= 20) ExceptionUtill.throwError(ExceptionEnum.ILL_CODE_DIE_LOOP, "safe>=" + 20);

        // 属性为对象的第一个属性,紧挨着左大括号
        if('{' == (obj2String.charAt(obj2String.length()-1))){
            obj2String = obj2String.substring(0, obj2String.length()-1);
            int indexEqual   = obj2String.lastIndexOf("=");
            int indexComma   = obj2String.lastIndexOf(",");
            int indexKuohaoL = obj2String.lastIndexOf("{");
            if(indexEqual > 0) {
                if(indexComma > indexKuohaoL){
                    String nameTmp = obj2String.substring(indexComma + 2, indexEqual);
                    FieldNode parent = new FieldNode(nameTmp, null, node, false);
                    node.setParent(parent);
                    LOGGER.debug(debugPre + "7.1=" + nameTmp);

                    obj2String = obj2String.substring(0, indexComma);
                    LOGGER.debug(debugPre + "7.2=" + obj2String);
                    fieldTree(parent, obj2String, false, safe);
                    return ;
                }else {
                    String nameTmp = obj2String.substring(indexKuohaoL + 1, indexEqual);
                    FieldNode parent = new FieldNode(nameTmp, null, node, false);
                    node.setParent(parent);
                    LOGGER.debug(debugPre + "8.1=" + nameTmp);

                    obj2String = obj2String.substring(0, indexKuohaoL+1);
                    LOGGER.debug(debugPre + "8.2=" + obj2String);
                    fieldTree(parent, obj2String, false, safe);
                    return ;
                }
            }else {
                // 到root了
                LOGGER.debug(debugPre + "9=" + obj2String);
                fieldTree(node, null, true, safe);
                return ;
            }
        }

        int indexKuohaoLL = obj2String.lastIndexOf("{");
        int indexKuohaoRR = obj2String.lastIndexOf("}");
        // 跳过左边的兄弟对象属性
        if(indexKuohaoRR>0 && indexKuohaoRR>indexKuohaoLL) {
            obj2String = obj2String.substring(0, indexKuohaoLL);
            LOGGER.debug(debugPre + "2=" + obj2String);
            fieldTree(node, obj2String, false, safe);
            return ;
        }

        if(indexKuohaoLL > 0) {
            obj2String = obj2String.substring(0, indexKuohaoLL);
            LOGGER.debug(debugPre + "3=" + obj2String);

            int indexEqual   = obj2String.lastIndexOf("=");
            int indexComma   = obj2String.lastIndexOf(",");
            int indexKuohaoL = obj2String.lastIndexOf("{");
            if(indexEqual > 0) {
                if(indexComma > indexKuohaoL){
                    String nameTmp = obj2String.substring(indexComma + 2, indexEqual);
                    FieldNode parent = new FieldNode(nameTmp, null, node, false);
                    node.setParent(parent);
                    LOGGER.debug(debugPre + "4.1=" + nameTmp);

                    obj2String = obj2String.substring(0, indexComma);
                    LOGGER.debug(debugPre + "4.2=" + obj2String);
                    fieldTree(parent, obj2String, false, safe);
                    return ;
                }else {
                    String nameTmp = obj2String.substring(indexKuohaoL + 1, indexEqual);
                    FieldNode parent = new FieldNode(nameTmp, null, node, false);
                    node.setParent(parent);
                    LOGGER.debug(debugPre + "5.1=" + nameTmp);

                    obj2String = obj2String.substring(0, indexKuohaoL+1);
                    LOGGER.debug(debugPre + "5.2=" + obj2String);
                    fieldTree(parent, obj2String, false, safe);
                    return ;
                }
            }else {
                // 到root了
                LOGGER.debug(debugPre + "6=" + obj2String);
                fieldTree(node, null, true, safe);
                return ;
            }
        }
    }



}
