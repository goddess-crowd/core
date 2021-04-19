package com.goddess.framwork.convertor;

import com.goddess.common.web.PageResult;
import com.goddess.framwork.api.dto.IRequestDto;
import com.goddess.framwork.api.dto.IResponseDto;
import com.goddess.framwork.domain.model.BaseEntity;
import com.goddess.framwork.infrastructure.persistence.po.BasePo;

import java.util.List;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 下午6:13
 * @Copyright © 女神帮
 */
public interface IConvertor<REQ extends IResponseDto,RES extends IRequestDto,
        E extends BaseEntity,P extends BasePo> {

    E req2Entity(REQ dto);
    P req2Po(REQ dto);
    REQ entity2Req(E entity);
    REQ po2Req(P po);
    E res2Entity(RES dto);
    P res2Po(RES dto);
    RES entity2Res(E entity);
    RES po2Res(P po);
    E po2Entity(P po);
    P entity2Po(E entity);

    List<E> reqList2EntityList(List<REQ> dtos);
    List<P> reqList2PoList(List<REQ> dtos);
    List<REQ> entityList2ReqList(List<E> entityList);
    List<REQ> poList2ReqList(List<P> poList);
    List<E> resList2EntityList(List<RES> dtos);
    List<P> resList2PoList(List<RES> dtos);
    List<RES> entityList2ResList(List<E> entityList);
    List<RES> poList2ResList(List<P> poList);
    List<E> poList2EntityList(List<P> poList);
    List<P> entityList2PoList(List<E> entityList);

    default PageResult<E> poPage2EntityPage(PageResult<P>  poPage) {
        if ( poPage == null) {
            return null;
        }

        PageResult<E> entityPage = new PageResult();
        entityPage.setPages( poPage.getPages());
        entityPage.setSize( poPage.getSize());
        entityPage.setTotal( poPage.getTotal());
        entityPage.setCurrent( poPage.getCurrent());
        entityPage.setRecords(poList2EntityList( poPage.getRecords()));

        return entityPage;
    }
    default PageResult<RES> entityPage2ResPage(PageResult<E>  entityPage) {
        if ( entityPage == null) {
            return null;
        }

        PageResult<RES> resDtoPage = new PageResult();
        resDtoPage.setPages( entityPage.getPages());
        resDtoPage.setSize( entityPage.getSize());
        resDtoPage.setTotal( entityPage.getTotal());
        resDtoPage.setCurrent( entityPage.getCurrent());
        resDtoPage.setRecords(entityList2ResList( entityPage.getRecords()));

        return resDtoPage;
    }
}