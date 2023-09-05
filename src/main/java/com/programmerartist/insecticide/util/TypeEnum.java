package com.programmerartist.insecticide.util;


/**
 * Created by 程序员Artist on 16/3/29.
 */
public enum TypeEnum {

    TYPE_OBJECT("object", "class java.lang.Object"),
    TYPE_BOOLEAN("boolean", "class java.lang.Boolean"),
    TYPE_STRING("string", "class java.lang.String"),
    TYPE_INTEGER("integer", "class java.lang.Integer"),
    TYPE_LONG("long", "class java.lang.Long"),
    TYPE_FLOAT("float", "class java.lang.Float"),
    TYPE_DOUBLE("double", "class java.lang.Double"),
    TYPE_SHORT("short", "class java.lang.short"),
    TYPE_CHAR("char", "class java.lang.Char");

    private String low;
    private String big;

    TypeEnum(String low, String big) {
        this.low = low;
        this.big = big;
    }

    public String getLow() {
        return low;
    }

    public String getBig() {
        return big;
    }

    /**
     *
     * @param type
     * @return
     */
    public static Class getType(String type) {
        ExceptionUtill.assertBlank(type);

        if(TYPE_OBJECT.getLow().equals(type) || TYPE_OBJECT.getBig().equals(type)) {

            return Object.class;
        }else if(TYPE_BOOLEAN.getLow().equals(type) || TYPE_BOOLEAN.getBig().equals(type)) {

            return TYPE_BOOLEAN.getLow().equals(type) ? boolean.class : Boolean.class;
        }else if(TYPE_STRING.getLow().equals(type) || TYPE_STRING.getBig().equals(type)) {

            return String.class;
        }else if(TYPE_INTEGER.getLow().equals(type) || TYPE_INTEGER.getBig().equals(type)) {

            return TYPE_INTEGER.getLow().equals(type) ? int.class : Integer.class;
        }else if(TYPE_LONG.getLow().equals(type) || TYPE_LONG.getBig().equals(type)) {

            return TYPE_LONG.getLow().equals(type) ? long.class : Long.class;
        }else if(TYPE_FLOAT.getLow().equals(type) || TYPE_FLOAT.getBig().equals(type)) {

            return TYPE_FLOAT.getLow().equals(type) ? float.class : Float.class;
        }else if(TYPE_DOUBLE.getLow().equals(type) || TYPE_DOUBLE.getBig().equals(type)) {

            return TYPE_DOUBLE.getLow().equals(type) ? double.class : Double.class;
        }else if(TYPE_SHORT.getLow().equals(type) || TYPE_SHORT.getBig().equals(type)) {

            return TYPE_SHORT.getLow().equals(type) ? short.class : Short.class;
        }else if(TYPE_CHAR.getLow().equals(type) || TYPE_CHAR.getBig().equals(type)) {

            return char.class;
        }

        return null;
    }

    /**
     *
     * @param type
     * @return
     */
    public static Object getObject(String type, Object fieldValue) {
        ExceptionUtill.assertBlank(type);

        if(TYPE_OBJECT.getLow().equals(type) || TYPE_OBJECT.getBig().equals(type)) {

        }else if(TYPE_BOOLEAN.getLow().equals(type) || TYPE_BOOLEAN.getBig().equals(type)) {

            fieldValue = Boolean.valueOf(fieldValue.toString());
        }else if(TYPE_STRING.getLow().equals(type) || TYPE_STRING.getBig().equals(type)) {

            fieldValue = String.valueOf(fieldValue.toString());
        }else if(TYPE_INTEGER.getLow().equals(type) || TYPE_INTEGER.getBig().equals(type)) {

            fieldValue = Integer.valueOf(fieldValue.toString());
        }else if(TYPE_LONG.getLow().equals(type) || TYPE_LONG.getBig().equals(type)) {

            fieldValue = Long.valueOf(fieldValue.toString());
        }else if(TYPE_FLOAT.getLow().equals(type) || TYPE_FLOAT.getBig().equals(type)) {

            fieldValue = Float.valueOf(fieldValue.toString());
        }else if(TYPE_DOUBLE.getLow().equals(type) || TYPE_DOUBLE.getBig().equals(type)) {

            fieldValue = Double.valueOf(fieldValue.toString());
        }else if(TYPE_SHORT.getLow().equals(type) || TYPE_SHORT.getBig().equals(type)) {

            fieldValue = Boolean.valueOf(fieldValue.toString());
        }else if(TYPE_CHAR.getLow().equals(type) || TYPE_CHAR.getBig().equals(type)) {
            //
        }

        return fieldValue;
    }
}
