package com.goddess.framwork.domain.model;


import com.goddess.framwork.infrastructure.persistence.po.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity<T> extends BasePo implements Serializable {

}
