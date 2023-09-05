package com.programmerartist.insecticide.util;

/**
 * 异常枚举
 *
 * Created by 程序员Artist on 16/3/22.
 */
public enum ExceptionEnum {

    /** 10-29       原生一级exception */
    NULL_POINTER(10, "NullPointer"),
    ILLEGAL_ARGUMENT(11, "IllegalArgument"),
    ARITHMETIC(12, "Arithmetic"),
    CLASS_CAST(13, "ClassCast"),
    MISSING_RESOURCE(14, "MissingResource"),
    NO_SUCH_ELEMENT(15, "NoSuchElement"),
    SECURITY(16, "Security"),
    ILLEGAL_STATE(17, "IllegalState"),
    INDEX_OUT_OF_BOUNDS(18, "IndexOutOfBounds"),


    /** 30-49       自定义一级exception */
    ILLEGAL_CODE(30, "IllegalCode"),
    UNSUPPORT(31, "UnSupport"),
    STARTUP_FAILED(32, "StartUpFailed"),


    /** 1000-4900   自定义二级exception,高两位为一级exception,低两位为二级exception */
    ILL_ARG_NULL(1100, "NullArgument: we find null param from input params"),
    ILL_ARG_BLANk(1101, "BlankArgument: we find blank param from input params"),

    ILL_CODE_DIE_LOOP(3000, "DieLoop: code maybe has die loop, we throw exception to protect application"),

    UNSUP_DOUBT(3100, "UnSupport_Doubt: we find doubt code, so throw exception to protect logic"),
    UNSUP_VIOLATE_PROMISE(3101, "UnSupport_ViolatePromise: code violate promise, so throw exception to protect logic"),

    STARTUP_FAIL_MISS_CONF(3200, "StartUp_Failed_Miss_Conf: miss some configure"),
    STARTUP_FAIL_ERROR(3201, "StartUp_Failed_Error: program error");



    private int code;
    private String msg;

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getValue() {
        return "[" + code + "] " + msg;
    }
}
