package com.goddess.common.exception;
/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
public abstract class ErrorCode {






    /**
     * 记录不存在
     */
    public static final String NOTFOUND = "001";

    /**
     * 不符合业务规则
     */
    public static final String DATA_ILLEGAL = "002";

    /**
     * 数据缺失
     */
    public static final String DATA_MISSING = "003";
    /**
     * 更新失败
     */
    public static final String UP_FAILED = "003";
    /**
     * 记录重复
     */
    public static final String DUPLICATION = "005";
    /**
     * mq消息错误
     */
    public static final String MQ_ERROR = "004";


    /**
     * 成功
     */
    public static final String CODE="000000";


}
