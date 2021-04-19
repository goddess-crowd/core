package com.goddess.framwork.application;

import com.goddess.common.exception.AppException;
import com.goddess.common.exception.ErrorCode;
import com.goddess.common.web.PageResult;
import com.goddess.framwork.api.dto.IRequestDto;
import com.goddess.framwork.api.dto.IResponseDto;
import com.goddess.framwork.convertor.IConvertor;
import com.goddess.framwork.infrastructure.repository.Repository;

import java.util.Map;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
public interface IApplicationService<T extends IRequestDto,E extends IResponseDto> {
    IConvertor getConvertor();
    Repository getRepository();

    default T save(E e) {
        return (T) getConvertor().entity2Res(getRepository().insert(getConvertor().req2Entity(e)));
    }
    /**
     * 删除（支持批量）
     * @param ids ids
     * @return boolean
     */
    default boolean deleteByIds(String ids) {
        return getRepository().deleteByIds(ids);
    }

    /**
     * 根据id查询一个对象
     * @param id id
     * @return T
     */
    default T selectById(Long id){
        return (T) getConvertor().entity2Res(getRepository().selectById(id));
    }
    /**
     * 分页查询
     * @param params 条件
     * @return Pager
     */
    default PageResult<T> getPage(Map<String, Object> params){
        return (PageResult<T>) getConvertor().entityPage2ResPage(getRepository().getPage(params));
    }
    /**
     * 更新
     * @param params 条件
     * @return Pager
     */
    default T updateByMap(Long id, Map<String, Object> params){
        boolean flag = getRepository().updateByMap(id, params);
        if(!flag){
            throw new AppException("更新失败",ErrorCode.UP_FAILED);
        }
        return selectById(id);
    }
}
