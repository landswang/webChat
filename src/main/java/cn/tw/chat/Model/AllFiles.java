/**
 * WdxcQywFiles.java
 * cn.tw.chat.Model
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月9日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package cn.tw.chat.Model;
/**
 * ClassName:WdxcQywFiles（Describe this Class）
 * @author   torreswang
 * @version  Ver 1.0
 * @Date	 2018年2月9日		下午11:50:52
 * @see 	 
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AllFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private long userid;

    private String filename;

    private String filepreview;

    private String filepath;

    private int deleted;

    private Date deletedtime;

    private Date createtime;

    private String filedownurl;

    private BigDecimal filesize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepreview() {
        return filepreview;
    }

    public void setFilepreview(String filepreview) {
        this.filepreview = filepreview;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * 删除状态 0：正常 1：已删除
     * 
     * @return
     */
    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedtime() {
        return deletedtime;
    }

    public void setDeletedtime(Date deletedtime) {
        this.deletedtime = deletedtime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 文件下载URL前缀（需拼写分片名称）
     * 
     * @return
     */
    public String getFiledownurl() {
        return filedownurl;
    }

    public void setFiledownurl(String filedownurl) {
        this.filedownurl = filedownurl;
    }

    /**
     * 文件大小
     * 
     * @return
     */
    public BigDecimal getFilesize() {
        return filesize;
    }

    public void setFilesize(BigDecimal bigDecimal) {
        this.filesize = bigDecimal;
    }
}
