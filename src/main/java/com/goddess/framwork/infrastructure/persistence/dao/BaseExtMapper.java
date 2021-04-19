package com.goddess.framwork.infrastructure.persistence.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
/**
 * @Description: <pre>扩展BaseMapper，需要用到此接口中的方法，继承此接口即可。
 *                 若自行写SQL,条件记得加上 "and deleted=fasle;"
 *                 </pre>
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
public interface BaseExtMapper<T> extends BaseMapper<T> {
}
