package com.goddess.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/7 上午9:35
 * @Copyright © 女神帮
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraParam implements Serializable {

    private static final long serialVersionUID = 1L;


    Long operatorId = -1L;


    String traceId;



}
