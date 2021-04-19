package com.goddess.common.mybatis.handler;



import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.goddess.common.util.ExtraParamUtil;
import com.goddess.common.util.SnowflakeIdWorker;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/8 下午5:55
 * @Copyright © 女神帮
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    Boolean deletedDefault = false;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public void insertFill(MetaObject metaObject) {

        Object id = getFieldValByName("id", metaObject);
        if (id == null) {
            setFieldValByName("id", snowflakeIdWorker.nextId(), metaObject);
        }

        Object createUserId = getFieldValByName("createUserId", metaObject);
        if (createUserId == null) {
            setFieldValByName("createUserId", ExtraParamUtil.getExtraParam().getOperatorId(), metaObject);
        }

        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }

        Object deleted = getFieldValByName("deleted", metaObject);
        if (deleted == null) {
            setFieldValByName("deleted", deletedDefault, metaObject);
        }

        Object modifyUserId = getFieldValByName("modifyUserId", metaObject);
        if (modifyUserId == null) {
            setFieldValByName("modifyUserId", ExtraParamUtil.getExtraParam().getOperatorId(), metaObject);

        }

        Object modifyTime = getFieldValByName("modifyTime", metaObject);
        if (modifyTime == null) {
            setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        Object modifyUserId = getFieldValByName("modifyUserId", metaObject);
        if (modifyUserId == null || Objects.equals(-1L,modifyUserId)) {
            setFieldValByName("modifyUserId", ExtraParamUtil.getExtraParam().getOperatorId(), metaObject);
        }

        setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
    }
}