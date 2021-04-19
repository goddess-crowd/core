package com.goddess.common.minio;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/12 下午10:30
 * @Copyright © 女神帮
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private String urlHttp;

    private String urlPath;
}