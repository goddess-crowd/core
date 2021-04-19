package com.goddess.common.util;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ID生成
 *
 * @author 子玄 Aidy.Q
 * @email Aidy.Q@bitsun-inc.com
 */

//@Component
public class IdGenerator implements IKeyGenerator {
    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;
    @Override
    public String executeSql(String s) {
        long nextId = IdWorkerFactory.getSnowflakeIdWorker("center").nextId();
        return "select " + nextId + " as id";

    }
}
