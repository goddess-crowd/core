package com.goddess.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goddess.common.util.NumberUtils;
import com.goddess.common.web.PageResult;
import com.goddess.framwork.infrastructure.persistence.FieldUtils;
import com.goddess.framwork.infrastructure.persistence.SQLFilter;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class Query<T> extends LinkedHashMap<String, Object> {
    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private long current = 1;
    /**
     * 每页条数
     */
    private long limit = 10;

    public Query(Map<String, Object> params) {
        this(NumberUtils.toLong(params.get("current")), NumberUtils.toLong(params.get("size")),
                StringUtils.isEmpty(params.get("searchCount")) ?true:Boolean.valueOf(params.get("searchCount").toString()), params);
    }

    public Query(Integer currentPage, Integer pageSize, Map<String, Object> params) {
        this(NumberUtils.toLong(currentPage), NumberUtils.toLong(pageSize),true, params);
    }

    public Query(Long currentPage, Long pageSize,boolean searchCount, Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        this.current = currentPage == null || currentPage < 1L ? 1L : currentPage;

        limit = pageSize == null || pageSize < 1L ? 10L : pageSize;

        this.put("offset", (this.current - 1) * limit);
        this.put("page", this.current);
        this.put("limit", limit);

        //mybatis-plus分页
        this.page = new Page<>(this.current, limit,searchCount);

        //排序,支持多字段排序
        String sorter = SQLFilter.sqlInject((String) params.get("sorter"), ",");
        if(StringUtils.isEmpty(sorter)){
            sorter="desc-createTime";
        }
        if (StringUtils.isEmpty(sorter)) {
            String[] sorters = sorter.split(",");
            for (String sorterSingle : sorters) {
                String[] sf = sorterSingle.split("-");
                if (sf.length == 2) {
                    String dbField = FieldUtils.toDBField(sf[1]);
                    if ("desc".equalsIgnoreCase(sf[0])) {
                        this.page.addOrder(OrderItem.desc(dbField));
                    } else if ("asc".equalsIgnoreCase(sf[0])) {
                        this.page.addOrder(OrderItem.asc(dbField));
                    }
                }
            }

        }


    }

    public Page<T> getPage() {
        return page;
    }

    public PageResult<T> getPager() {
        return new PageResult(page);
    }


    public long getLimit() {
        return limit;
    }
}
