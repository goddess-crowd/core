package com.goddess.common.minio;


import io.minio.errors.MinioException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;


/**
 * @author 失败女神
 * @email: 18733123202@163.com
 * @date 2021/4/12 下午10:32
 * @Copyright © 女神帮
 */

@Slf4j
@Api(tags = {"文件管理"})
@RestController
@RequestMapping("/file")
@ConditionalOnProperty(prefix = "goddess", name = "minio.enable", havingValue = "true")
public class MinioController {

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件",tags = {"文件管理"}, nickname = "upload")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "000000:成功，否则失败")})
    @PostMapping(value = "/upload/{bucketName}", produces = {"application/json"})
    public FileUploadResponse upload(@ApiParam(value = "文件", required = true)@RequestPart("file") MultipartFile file,
                                     @ApiParam(value = "桶名称", required = true) @PathVariable("bucketName")  String bucketName) {
        FileUploadResponse response = null;
        try {
            response = minioUtil.uploadFile(file, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败 : [{}]", Arrays.asList(e.getStackTrace()));
        }
        return response;
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete/{bucketName}/{objectName}")
    @ApiOperation(value = "删除文件",tags = {"文件管理"}, nickname = "delete")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "000000:成功，否则失败")})
    public void delete(@ApiParam(value = "文件名称", required = true) @PathVariable("objectName") String objectName,
                       @ApiParam(value = "桶名称", required = true) @PathVariable("bucketName")  String bucketName) throws Exception {
        minioUtil.removeObject(bucketName, objectName);
        System.out.println("删除成功");
    }

    /**
     * 下载文件到本地
     */
    @GetMapping("/download/{bucketName}/{objectName}")
    @ApiOperation(value = "下载文件",tags = {"文件管理"}, nickname = "download")
    public ResponseEntity<byte[]> downloadToLocal(
            @ApiParam(value = "文件名称", required = true) @PathVariable("objectName") String objectName,
            @ApiParam(value = "桶名称", required = true) @PathVariable("bucketName")  String bucketName,
            HttpServletResponse response) throws Exception {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream stream = null;
        ByteArrayOutputStream output = null;
        try {
            stream = minioUtil.getObject(bucketName, objectName);
            if (stream == null) {
                System.out.println("文件不存在");
            }
            //用于转换byte
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = stream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] bytes = output.toByteArray();

            //设置header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Accept-Ranges", "bytes");
            httpHeaders.add("Content-Length", bytes.length + "");
//            objectName = new String(objectName.getBytes("UTF-8"), "ISO8859-1");
            //把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
            httpHeaders.add("Content-disposition", "attachment; filename=" + objectName);
            httpHeaders.add("Content-Type", "text/plain;charset=utf-8");
//            httpHeaders.add("Content-Type", "image/jpeg");
            responseEntity = new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.CREATED);

        } catch (MinioException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (output != null) {
                output.close();
            }
        }
        return responseEntity;
    }

    /**
     * 在浏览器预览图片
     */
    @GetMapping("/preViewPicture/{bucketName}/{objectName}")
    @ApiOperation(value = "预览图片",tags = {"文件管理"}, nickname = "preViewPicture")
    public void preViewPicture(@ApiParam(value = "文件名称", required = true) @PathVariable("objectName") String objectName,
                               @ApiParam(value = "桶名称", required = true) @PathVariable("bucketName")  String bucketName,
                               HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");
        try (ServletOutputStream out = response.getOutputStream()) {
            InputStream stream = minioUtil.getObject(bucketName, objectName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = stream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] bytes = output.toByteArray();
            out.write(bytes);
            out.flush();
        }
    }
}