package com.goddess.framwork.infrastructure.persistence;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goddess.common.web.PageResult;

import java.util.List;
import java.util.Map;

public interface IPService<P> extends IService<P> {
    /**
     * 单表查询
     * @param params
     * @param clz
     * @return
     */
    PageResult<P> queryPage(Map<String, Object> params, Class clz);
    /**
     * 单表查询
     * @param currentPage
     * @param pageSize
     * @param params
     * @param clz
     * @return
     */
    PageResult<P> queryPage(Long currentPage, Long pageSize, Map<String, Object> params, Class clz);
    /**
     * 单表查询
     */
    List<P> getList(Map<String, Object> params, Class clz);
}
