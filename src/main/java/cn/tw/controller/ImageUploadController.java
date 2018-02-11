/**
 * ImageUploadController.java
 * cn.tw.controller
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月10日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package cn.tw.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:ImageUploadController（Describe this Class）
 * 
 * @author torreswang
 * @version Ver 1.0
 * @Date 2018年2月10日 上午10:11:26
 * @see
 */

@Slf4j
@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageUploadController {

    @Value("${AREA_FILE}")
    private String AREA_FILE;// excel下载文件名

    @Value("${AREA_DIR}")
    private String AREA_DIR;// excel临时存储文件夹

    @Value("${WEB_PATH}")
    private String WEB_PATH;// 访问路径

    /**
     * 上传文件
     * 
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/save", produces = { "application/json" })
    public Map<String, String> importExcel(@RequestParam MultipartFile file, HttpServletRequest request)
            throws IOException {
        // AREA_DIR = request.getSession().getServletContext().getRealPath("/");
        // 根目录路径
        // String AREA_DIR = request.getRealPath("/") + "";
        // 根目录URL
        // String rootUrl = request.getContextPath() + "/";
        // 获取文件名
        // C:\Users\torre\AppData\Local\Temp\tomcat-docbase.7123668067352414378.9090\
        // C:\Users\torre\AppData\Local\Temp\tomcat-docbase.6816546927956999191.9090\attached/
        String originalFilename = file.getOriginalFilename();
        String md5 = "d41d8cd98f00b204e9800998ecf8427e";

        // 合并文件
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        try {
            File dirFile = new File(AREA_DIR, originalFilename);
            // 以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(file.getInputStream());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
            // d41d8cd98f00b204e9800998ecf8427e
            // FileUtils.uploadFile(buf, AREA_DIR, originalFilename);
            // md5 = FileUtils.copyInputStreamToFile(inputStream, dirFile);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        String result = "";
        int res = -1;
        // 返回提示信息
        map.put("result", result);
        map.put("fileName", WEB_PATH + originalFilename);
        map.put("filemd5", md5);
        return map;
    }

}
