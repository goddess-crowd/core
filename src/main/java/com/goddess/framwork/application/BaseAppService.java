package com.goddess.framwork.application;

import com.goddess.framwork.convertor.IConvertor;
import com.goddess.framwork.infrastructure.repository.Repository;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/9 上午10:26
 * @Copyright © 女神帮
 */
public class BaseAppService {
    private IConvertor iConvertor;
    private Repository repository;

    public BaseAppService( IConvertor iConvertor,Repository repository) {
        this.repository = repository;
        this.iConvertor = iConvertor;
    }
    public IConvertor getConvertor() {
        return iConvertor;
    }
    public Repository getRepository() {
        return repository;
    }
}
