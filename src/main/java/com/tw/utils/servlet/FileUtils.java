/**
 * FileUtils.java
 * com.tw.utils.servlet
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月9日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package com.tw.utils.servlet;
/**
 * ClassName:FileUtils（Describe this Class）
 * @author   torreswang
 * @version  Ver 1.0
 * @Date	 2018年2月9日		下午11:51:48
 * @see 	 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.util.Map;

import cn.tw.chat.Model.AllFiles;

/**
 * 文件处理工具类
 */
public class FileUtils {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 保存文件流到文件,同时返回文件MD5
     * 
     * @param inputStream
     * @param file
     */
    public static String copyInputStreamToFile(InputStream inputStream, File file) {
        MessageDigest md = null;
        try {
            if (inputStream == null || inputStream == null) {
                return null;
            }
            md = MessageDigest.getInstance("MD5");
            byte[] b = new byte[102400];// set b 100Kb byte.
            int n = inputStream.read(b);
            int totalBytes = n;
            FileOutputStream fos = new FileOutputStream(file);
            while (n > -1) {
                fos.write(b, 0, n);
                fos.flush();
                n = inputStream.read(b);
                totalBytes += n;
            }
            fos.close();
            inputStream.close();
            System.out.println("文件总大小：" + totalBytes);
            // 生成文件MD5值
            FileInputStream in = new FileInputStream(file);
            // 文件内存映射，提高读写超大文件可能和速度，但会造成文件锁定不可操作。
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            md.update(byteBuffer);
            clean(byteBuffer);

            byte[] encrypt = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < encrypt.length; i++) {
                String hex = Integer.toHexString(0xff & encrypt[i]);
                if (hex.length() == 1)
                    sb.append('0');
                sb.append(hex);
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件合并
     * 
     * @param QywFile
     * @param chunkFileMap
     */
    public static void fileCompose(AllFiles QywFile, Map<String, File> chunkFileMap) {
        String path = QywFile.getFilepath();
        File mainFile = new File(path);
        if (!mainFile.getParentFile().exists()) {
            mainFile.getParentFile().mkdirs();
        }
        try {
            FileChannel out = new FileOutputStream(mainFile).getChannel();
            FileChannel in = null;
            long start = System.currentTimeMillis();
            for (int i = 0; i < chunkFileMap.size(); i++) {
                File file = chunkFileMap.get(String.valueOf(i));
                System.out.println("file=" + file.getName());
                in = new FileInputStream(file).getChannel();
                MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                out.write(buf);
                in.close();
                FileUtils.clean(buf);
            }
            System.out.println("文件大小：" + mainFile.length() / 1024 / 1024 + " M");
            QywFile.setFilesize(new BigDecimal(mainFile.length()));
            long end = System.currentTimeMillis();
            System.out.println("常规方法合并耗时：" + (end - start) / 1000 + " 秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
