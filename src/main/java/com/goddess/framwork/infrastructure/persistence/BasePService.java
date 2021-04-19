package com.goddess.framwork.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goddess.common.mybatis.Query;
import com.goddess.common.web.PageResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据库层基础聚合类继, 承mybatis plus的ServiceImpl，将前端传递的查询map直接构造Warpper
 */
public class BasePService<M extends BaseMapper<P>, P> extends ServiceImpl<M, P> implements IPService<P> {

    public BasePService(M baseMapper) {
        this.baseMapper = baseMapper;
    }


    @Override
    public List<P> getList(Map<String, Object> params, Class clz) {
        return this.list(QueryParamUtils.getEntityWrapper(params, clz));
    }

    @Override
    public PageResult<P> queryPage(Map<String, Object> params, Class clz) {
        Page<P> page =
            (Page<P>)this.page(new Query<P>(params).getPage(), QueryParamUtils.getEntityWrapper(params, clz));
        return new PageResult<>(page);
    }


    @Override
    public PageResult<P> queryPage(Long currentPage, Long pageSize, Map<String, Object> params, Class clz) {
        Query query = new Query(currentPage, pageSize,true, params);
        Page<P> page = (Page<P>)this.page(query.getPage(), QueryParamUtils.getEntityWrapper(params, clz));

        return new PageResult(page);
    }

    /**
     * 修复Batch批量操作的全部方法,重写currentModelClass,要不会返回null. 当super.currentModelClass方法取不到Po类型时(即返回Object),再尝试从代理对象里取po类型.
     */
    @Override
    protected Class currentModelClass() {
        Class clz = super.currentModelClass();
        if (clz instanceof Object) {
            clz = currentModelClassFromObject();
        }
        return clz;
    }

    /**
     * Batch批量操作时,由于我们的Po没有写一个继承serviceImpl的子类(全部使用BasePService泛型类处理) ,而原有的currentModelClass方法是从静态类里拿PO的,所以会拿不到
     * ,现在增加一个从代理对象里拿PO的方法(当currentModelClass方法取不到时才调用),不影响原有逻辑的走向
     *
     * @return Po对象的类
     */
    private Class currentModelClassFromObject() {
        try {
            BaseMapper<P> mp = this.baseMapper;
            /**
             * 获取真正的代理类(h是java.lang.reflect.Proxy类中InvocationHandler类型私有属性)
             * mp.getClass():内存自动生成的代理类(是Proxy的子类,如:com.sun.proxy.$Proxy106)
             * mp.getClass().getSuperclass():java.lang.reflect.Proxy
             * getDeclaredField("h"):获取java.lang.reflect.Proxy类中InvocationHandler类型的私有属性h
             * h.get(mp):取到真正的代理类(PageMapperProxy)
             */
            Field h = mp.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            InvocationHandler mapperProxy = (InvocationHandler)h.get(mp);
            /**
             * 获取Class类型的mapperInterface对象(mapperInterface是PageMapperProxy类中Class类型私有属性)
             *
             */
            Field mapperInterface = mapperProxy.getClass().getDeclaredField("mapperInterface");
            mapperInterface.setAccessible(true);
            Class mapperInterfaceObject = (Class)mapperInterface.get(mapperProxy);
            /**
             * 获取ClassRepository类型的genericInfo对象(genericInfo是Class类中ClassRepository类型私有属性)
             */
            Field genericInfo = mapperInterfaceObject.getClass().getDeclaredField("genericInfo");
            genericInfo.setAccessible(true);
            Object genericInfoObj = genericInfo.get(mapperInterfaceObject);
            /**
             * 获取superInterfaces对象(superInterfaces是ClassRepository类中的Type[]类型私有属性
             */
            Field superInterfaces = genericInfoObj.getClass().getDeclaredField("superInterfaces");
            superInterfaces.setAccessible(true);
            Type[] superInterfacesObj = (Type[])superInterfaces.get(genericInfoObj);
            /**
             * 获取com.baomidou.mybatisplus.core.mapper.BaseMapper<真正的Po>
             */
            ParameterizedType superInterfaceObj = (ParameterizedType)superInterfacesObj[0];
            /**
             * 获取真正的Po
             */
            Type[] types = superInterfaceObj.getActualTypeArguments();
            Class clz = (Class)types[0];
            return clz;
        } catch (Exception e) {
            return Object.class;
        }
    }

}
