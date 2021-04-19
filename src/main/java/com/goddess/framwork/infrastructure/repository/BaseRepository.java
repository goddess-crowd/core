package com.goddess.framwork.infrastructure.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goddess.framwork.convertor.IConvertor;
import com.goddess.framwork.infrastructure.persistence.BasePService;
import com.goddess.framwork.infrastructure.persistence.IPService;
import com.goddess.framwork.infrastructure.persistence.dao.BaseExtMapper;
import com.goddess.framwork.infrastructure.persistence.po.BasePo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/8 上午10:21
 * @Copyright © 女神帮
 */
public class BaseRepository<P extends BasePo> {

    private IPService<P> iPService;
    private IConvertor iConvertor;
    private BaseMapper<P> baseMapper;

    public BaseRepository(IConvertor iConvertor, BaseMapper baseMapper) {
        this.iPService = new BasePService(baseMapper);
        this.iConvertor = iConvertor;
        this.baseMapper = baseMapper;
    }

    public IConvertor getConvertor() {
        return iConvertor;
    }

    public BaseMapper getMapper() {
        return baseMapper;
    }

    public IPService getIPService() {
        return iPService;
    }
}
