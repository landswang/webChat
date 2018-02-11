/**
 * UploadImageSlt.java
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
 * ClassName:UploadImageSlt（Describe this Class）
 * @author   torreswang
 * @version  Ver 1.0
 * @Date	 2018年2月9日		下午11:37:00
 * @see 	 
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import cn.tw.chat.Model.AllFiles;
import net.sf.json.JSONObject;

public class UploadImageSlt extends HttpServletBasic {

    private static final long serialVersionUID = 1L;

    /**
     * 文件上传
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void fileupload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("=================fileupload===================");
            response.addHeader("Access-Control-Allow-Origin", "*");
            String useridStr = request.getParameter("tokenid");
            if (useridStr == null || "".equals(useridStr) || useridStr.length() < 3) {
                response.getWriter()
                        .write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'token校验错误'}").toString());
                return;
            }
            long userid = Long.parseLong(useridStr);
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                // 得到所有的表单域，它们目前都被当作FileItem
                // List<FileItem> fileItems = upload.parseRequest(request);

                String id = "";
                String fileName = "";
                // 如果大于1说明是分片处理
                int chunks = 1;
                int chunk = 0;
                FileItem tempFileItem = null;
                // for (FileItem fileItem : fileItems) {
                // if (fileItem.getFieldName().equals("id")) {
                // id = fileItem.getString();
                // } else if (fileItem.getFieldName().equals("name")) {
                // fileName = new
                // String(fileItem.getString().getBytes("ISO-8859-1"), "UTF-8");
                // } else if (fileItem.getFieldName().equals("chunks")) {
                // chunks = NumberUtils.toInt(fileItem.getString());
                // } else if (fileItem.getFieldName().equals("chunk")) {
                // chunk = NumberUtils.toInt(fileItem.getString());
                // } else if (fileItem.getFieldName().equals("file")) {
                // tempFileItem = fileItem;
                // }
                // }
                System.out.println("id=" + id + "  filename=" + fileName + "  chunks=" + chunks + " chunk=" + chunk
                        + "  size=" + tempFileItem.getSize());
                // 临时目录用来存放所有分片文件
                String tempFileDir = getTempFilePath() + File.separator + userid;
                File parentFileDir = new File(tempFileDir);
                if (!parentFileDir.exists()) {
                    parentFileDir.mkdirs();
                }
                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                File tempPartFile = new File(parentFileDir, fileName + "_" + chunk + ".part");

                String MD5 = FileUtils.copyInputStreamToFile(tempFileItem.getInputStream(), tempPartFile);
                int count = 0;
                while (MD5 == null && count < 3) {
                    MD5 = FileUtils.copyInputStreamToFile(tempFileItem.getInputStream(), tempPartFile);
                    count++;
                }
                if (MD5 == null) {
                    throw new Exception("分片文件：" + chunk + "上传失败");
                }
                // 分片文件上传成功以后，重命名分片文件，规则：原名之前拼接MD5码
                tempPartFile.renameTo(new File(parentFileDir, fileName + "_" + chunk + "_" + MD5 + ".part"));
                System.out.println("MD5=" + MD5);
                response.getWriter().write(JSONObject.fromObject("{\"md5\":\"" + MD5 + "\"}").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重复验证
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void md5Validation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("=================md5Validation===================");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("utf-8");
            String useridStr = request.getParameter("tokenid");
            if (useridStr == null || "".equals(useridStr) || useridStr.length() < 3) {
                response.getWriter()
                        .write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'token校验错误'}").toString());
                return;
            }
            long userid = Long.parseLong(useridStr);
            String tempFileDir = getTempFilePath() + File.separator + userid;
            File parentFileDir = new File(tempFileDir);
            if (!parentFileDir.exists()) {
                response.getWriter().write(JSONObject.fromObject("{\"md5_arr\":\"\"}").toString());
                return;
            }
            String fileName = request.getParameter("name");
            fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
            System.out.println("fileName=" + fileName);

            StringBuffer sb = new StringBuffer();
            for (File file : parentFileDir.listFiles()) {
                String name = file.getName();
                if (name.endsWith(".part") && name.startsWith(fileName)) {
                    // 此文件有上传记录，解析MD5
                    name = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf(".part"));
                    if (name.length() == 32) {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(name);
                    }
                }
            }
            response.getWriter().write(JSONObject.fromObject("{\"md5_arr\":\"" + sb.toString() + "\"}").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件所有分片上传完毕之后，保存文件数据到数据库
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void composeFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("=================composeFile===================");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("utf-8");
            String useridStr = request.getParameter("tokenid");
            if (useridStr == null || "".equals(useridStr) || useridStr.length() < 3) {
                response.getWriter()
                        .write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'token校验错误'}").toString());
                return;
            }
            long userid = Long.parseLong(useridStr);
            String tempFileDir = getTempFilePath() + File.separator + userid;
            File parentFileDir = new File(tempFileDir);
            if (!parentFileDir.exists()) {
                response.getWriter().write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'目录不存在'}").toString());
                return;
            }
            String fileName = request.getParameter("name");
            fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
            String chunks = request.getParameter("chunks");
            System.out.println("fileName=" + fileName);
            Map<String, File> chunkFileMap = new HashMap<String, File>();

            String md5 = null;
            for (File file : parentFileDir.listFiles()) {
                String name = file.getName();
                if (name.endsWith(".part") && name.startsWith(fileName)) {
                    md5 = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf(".part"));
                    System.out.println("md5=" + md5);
                    if (md5.length() == 32) {// 完整的分片文件
                        String index = name.replace(fileName, "").replace("_" + md5 + ".part", "").replace("_", "");
                        chunkFileMap.put(index, file);
                    }
                }
            }
            // 分片总数和分片文件数一致，则证明分片文件已完整上传，可以持久化数据
            if (chunkFileMap.size() == Integer.parseInt(chunks.trim())) {
                System.out.println("===========开始合并文件分片==========");
                AllFiles QywFile = new AllFiles();
                QywFile.setFilename(fileName);
                QywFile.setCreatetime(new Date());
                QywFile.setFilepath("/site/photos/file/" + userid + "/" + fileName);
                QywFile.setFiledownurl("http://www.sssss.cn/file/" + userid + "/" + fileName);

                // AI、PDF、EPS、CDR、PSD、JPG、TIFF
                if (fileName.toLowerCase().endsWith(".tif")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/TIF.jpg");
                } else if (fileName.toLowerCase().endsWith(".jpg")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/JPG.jpg");
                } else if (fileName.toLowerCase().endsWith(".psd")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/PSD.jpg");
                } else if (fileName.toLowerCase().endsWith(".ai")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/AI.jpg");
                } else if (fileName.toLowerCase().endsWith(".cdr")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/CDR.jpg");
                } else if (fileName.toLowerCase().endsWith(".zip")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/ZIP.jpg");
                } else if (fileName.toLowerCase().endsWith(".pdf")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/PDF.jpg");
                } else if (fileName.toLowerCase().endsWith(".png")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/PNG.jpg");
                } else if (fileName.toLowerCase().endsWith(".rar")) {
                    QywFile.setFilepreview("http://www.wodexiangce.cn/images/type/RAR.jpg");
                }
                QywFile.setUserid(userid);
                FileUtils.fileCompose(QywFile, chunkFileMap);// 合并文件
                File file = new File(QywFile.getFilepath());
                if (!file.exists()) {
                    System.out.println("文件合并失败：" + QywFile.getFilepath());
                    response.getWriter()
                            .write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'文件合并失败'}").toString());
                    return;
                }

                // 把文件路径保存到数据库
                // QywFileService fileService = (QywFileService)
                // wac.getBean("qywFileService");
                // Long fileid = (Long) fileService.saveQywFile(QywFile);
                // System.out.println("文件保存成功：" + fileid);

                response.getWriter().write(JSONObject
                        .fromObject("{\"status\":\"success\",'url':'" + QywFile.getFiledownurl() + "'}").toString());
            } else {
                System.out.println("分片数量不正确,实际分片数量：" + chunkFileMap.size() + " 总分片数量：" + chunks);
                response.getWriter()
                        .write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'分片数量不正确'}").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("文件合并失败：" + e.getMessage());
            response.getWriter().write(JSONObject.fromObject("{\"status\":\"error\",'errstr':'文件合并失败'}").toString());
        }
    }

    /**
     * 指定临时目录
     * 
     * @return
     */
    private String getTempFilePath() {
        return "/site/xxxxxx/temp";
    }

}
