package com.programmerartist.insecticide.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具集
 *
 * Created by 程序员Artist on 16/2/25.
 */
public class ReflectUtill {

    /**
     * 从对象list里抽属性,返回list<属性>
     *
     * @param objList
     * @param paramName
     * @param <T>
     */
    public static <T, V> List<V> drill(List<T> objList, String paramName) throws Exception {
        if(ExceptionUtill.containsBlank(objList, paramName)) return new ArrayList<>();

        List<V> values = new ArrayList<V>();
        Class objClazz = objList.get(0).getClass();

        String getter = getGetter(paramName);
        if(null!=objClazz.getDeclaredField(paramName) && null!=objClazz.getDeclaredField(paramName).getType()
                && objClazz.getDeclaredField(paramName).getType().toString().equals(TypeEnum.TYPE_BOOLEAN.getLow())) {
            getter = getIsser(paramName);
        }

        for (T t : objList) {
            Object value = objClazz.getMethod(getter, null).invoke(t);
            if (null != value) values.add((V) value);
        }

        return values;
    }

    /**
     * 从对象里抽一个属性
     *
     * @param obj
     * @param paramName
     * @param <T>
     */
    public static <T, V> V drill(T obj, String paramName) throws Exception {
        if(ExceptionUtill.containsBlank(obj, paramName)) return null;

        Class objClazz = obj.getClass();

        String getter = getGetter(paramName);
        if(null!=objClazz.getDeclaredField(paramName) && null!=objClazz.getDeclaredField(paramName).getType()
                && objClazz.getDeclaredField(paramName).getType().toString().equals(TypeEnum.TYPE_BOOLEAN.getLow())) {
            getter = getIsser(paramName);
        }
        Object value = objClazz.getMethod(getter, null).invoke(obj);

        return (V)value;
    }

    /**
     * 从对象里抽一个属性(属性可以不是obj的直接属性,而是obj的对象属性的内部属性;且支持多层,即可以是非常内部的属性名)
     *
     * 注意: 如果属性名paramName有多个,则不支持,直接报错(为了保证逻辑的绝对正确性)
     *
     * @param obj
     * @param paramName
     * @param <V>
     */
    public static <V> V drillDeep(Object obj, String paramName) throws Exception {
        if(ExceptionUtill.containsBlank(obj, paramName)) return null;
        if(!FindUtill.containsField(paramName, obj)) return null;

        FieldNode node = FindUtill.getFieldTree(paramName, obj.toString());
        node = node.toRoot();

        int self = 0;
        Object objIte = obj;
        for(;;) {
            objIte = drill(objIte, node.getName());
            if(node.isSelf()) break ;

            node = node.getChild();

            self++;
            if(self >= 20) {
                ExceptionUtill.throwError(ExceptionEnum.ILL_CODE_DIE_LOOP, "safe>=" + 20);
            }
        }

        return (V)objIte;
    }

    /**
     * 从对象list里按某一个属性, 抽成一个map
     *
     * @param objList
     * @param keyParamName
     * @param <V>
     */
    public static <K, V> Map<K, V> drill2Map(List<V> objList, String keyParamName) throws Exception {
        if(ExceptionUtill.containsBlank(objList, keyParamName)) return new HashMap<>();

        Map<K, V> key2Obj = new HashMap<>();
        Class objClazz = objList.get(0).getClass();

        for (V v : objList) {
            Object value = objClazz.getMethod(getGetter(keyParamName), null).invoke(v);
            if (null != value) key2Obj.put((K) value, v);
        }

        return key2Obj;
    }

    /**
     * 从对象list里按某一个属性, 抽成一个map<keyParamName, valueParamName>
     *
     * @param objList
     * @param keyParamName
     * @param valueParamName
     * @param <V>
     */
    public static <K, V, O> Map<K, V> drill2Map(List<O> objList, String keyParamName, String valueParamName) throws Exception {
        if(ExceptionUtill.containsBlank(objList, keyParamName, keyParamName)) return new HashMap<>();
        if(ExceptionUtill.containsNull(objList.get(0))) return new HashMap<>();

        Map<K, V> key2Value = new HashMap<>();
        Class objClazz = objList.get(0).getClass();

        for (O o : objList) {
            Object key   = objClazz.getMethod(getGetter(keyParamName), null).invoke(o);
            Object value = objClazz.getMethod(getGetter(valueParamName), null).invoke(o);
            if (null!=key || null!=value) {
                key2Value.put((K) key, (V) value);
            }
        }

        return key2Value;
    }

    /**
     *
     * @param object
     * @param separator
     * @param <O>
     * @return
     * @throws Exception
     */
    public static <O> String obj2String(O object, String separator) throws Exception {
        if(ExceptionUtill.containsNull(object) || ExceptionUtill.containsBlank(separator)) return "";

        List<String> values = new ArrayList<>();

        Class objClazz = object.getClass();
        Field[] fields = objClazz.getDeclaredFields();

        for(Field field : fields){
            Object value = objClazz.getMethod(getGetter(field.getName()), null).invoke(object);
            values.add(null == value ? "" : value.toString());
        }

        return StringUtils.join(values.iterator(), separator);
    }


    /**
     * 抽取对象里的所有 paramName 2 paramValue 的map
     *
     * @param object
     * @param <P>
     * @return
     */
    public static <P> Map<String, Object> drillParam(P object) throws Exception {
        if(ExceptionUtill.containsNull(object)) return new HashMap<>();

        Map<String, Object> name2Values = new HashMap<>();

        Class objClazz = object.getClass();
        Field[] fields = objClazz.getDeclaredFields();

        for(Field field : fields){
            Object value = objClazz.getMethod(getGetter(field.getName()), null).invoke(object);
            name2Values.put(field.getName(), value);
        }

        return name2Values;
    }


    /**
     * 补齐字段
     *
     * 从source对象里摘出非null字段,插入覆盖到target对象里,返回target对象
     *
     * @param target
     * @param source
     * @param <O>
     * @return
     * @throws Exception
     */
    public static <O> O fillField(O target, O source) throws Exception {
        if(ExceptionUtill.containsNull(target, source)) return null;

        Class clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            Object value = clazz.getMethod(getGetter(field.getName()), null).invoke(source);
            if(null == value) continue;

            clazz.getMethod(getSetter(field.getName()), value.getClass()).invoke(target, value);
        }

        return target;
    }

    /**
     * 补齐字段
     *
     * 从source对象里摘出非null字段,插入覆盖到target对象里(null属性),返回target对象
     *
     * @param target
     * @param source
     * @param <O>
     * @return
     * @throws Exception
     */
    public static <O> O fillField2NullTarget(O target, O source) throws Exception {
        if(ExceptionUtill.containsNull(target, source)) return null;

        Class clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            Object svalue = clazz.getMethod(getGetter(field.getName()), null).invoke(source);
            Object tvalue = clazz.getMethod(getGetter(field.getName()), null).invoke(target);
            if(null==tvalue && svalue!=null) {
                clazz.getMethod(getSetter(field.getName()), svalue.getClass()).invoke(target, svalue);
            }
        }

        return target;
    }

    /**
     * 给对象target的某字段名fieldName设置值为fieldValue
     *
     * @param target
     * @param fieldName
     * @param fieldValue
     * @param <O>
     * @return
     * @throws Exception
     */
    public static <O> O setField(O target, String fieldName, Object fieldValue) throws Exception {
        if(ExceptionUtill.containsBlank(target, fieldName, fieldValue)) return target;

        Class clazz = target.getClass();
        if(null == clazz.getDeclaredField(fieldName)) return target;

        String type = clazz.getDeclaredField(fieldName).getType().toString();
        Class fieldClazz = TypeEnum.getType(type);
        fieldValue = TypeEnum.getObject(type, fieldValue);

        String setter = getSetter(fieldName);
        if(null == fieldClazz) {
            fieldClazz = fieldValue.getClass();
        }

        clazz.getMethod(setter, fieldClazz).invoke(target, fieldValue);

        return target;
    }

    /**
     *
     * @param objects
     * @param separator
     * @param <O>
     * @return
     * @throws Exception
     */
    public static <O> List<String> toString(List<O> objects, String separator) throws Exception {
        if(ExceptionUtill.containsBlank(objects, separator)) return new ArrayList<>();

        List<String> values = new ArrayList<>();

        for(O object : objects) {
            values.add(obj2String(object, separator));
        }

        return values;
    }

    /**
     *
     * @param paramName
     * @return
     */
    public static String getGetter(String paramName) {
        if(StringUtils.isBlank(paramName)) return "";

        return "get" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
    }

    /**
     *
     * @param paramName
     * @return
     */
    public static String getSetter(String paramName) {
        if(StringUtils.isBlank(paramName)) return "";

        return "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
    }

    /**
     *
     * @param paramName
     * @return
     */
    public static String getIsser(String paramName) {
        if(StringUtils.isBlank(paramName)) return "";

        return "is" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
    }






    //========================================= main eg: =============================================================

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // drill()
        List<Object> peoples = new ArrayList<>();
        List<String> peopleNames = ReflectUtill.drill(peoples, "name");

        // drillToMap
        Map<Long, Object> id2Peoples = ReflectUtill.drill2Map(peoples, "id");

    }

}
