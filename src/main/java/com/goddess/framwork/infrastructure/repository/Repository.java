package com.goddess.framwork.infrastructure.repository;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goddess.common.exception.AppException;
import com.goddess.common.web.PageResult;
import com.goddess.framwork.convertor.IConvertor;
import com.goddess.framwork.domain.model.BaseEntity;
import com.goddess.framwork.infrastructure.persistence.IPService;
import com.goddess.framwork.infrastructure.persistence.QueryParamUtils;
import com.goddess.framwork.infrastructure.persistence.po.BasePo;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Repository<T extends BaseEntity,P extends BasePo> {

    IConvertor getConvertor();
    BaseMapper getMapper();
    IPService getIPService();
    Class getPoClass();

    default T insert(T t){
        BasePo basePo = getConvertor().entity2Po(t);
        getIPService().save(basePo);
        return (T) getConvertor().po2Entity(basePo);
    }

    default boolean update(T t){
        return getIPService().updateById(getConvertor().entity2Po(t));
    }

    default boolean delete(Long id){
        return getIPService().removeById(id);
    }

    default T selectById(Long id){
        return (T) getConvertor().po2Entity((P) getIPService().getById(id));
    }

    default boolean deleteByIds(String ids) {
        try {
            List<Long> idLongList =
                    Arrays.asList(ids.split(",")).stream().map(s -> NumberUtils.createLong(s.trim())).collect(Collectors.toList());
            return getIPService().removeByIds(idLongList);
        } catch (Exception e) {
            throw new AppException("参数错误：" + ids, e);
        }
    }
    default PageResult<T> getPage(Map<String, Object> params){
        PageResult<P> poPage = getIPService().queryPage(params,getPoClass());
        return getConvertor().poPage2EntityPage(poPage);
    }
    default boolean updateByMap(Long id, Map<String, Object> params){
        UpdateWrapper<P> updateWrapper = QueryParamUtils.updateWrapper4Map(getPoClass(), id, params);

        try {
            return getIPService().update(getPoClass().newInstance(), updateWrapper);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
