package cn.tw.mongo.model;

import java.util.Date;

import javax.persistence.Id;

import lombok.Data;

/**
 * 基本消息类型 Created by torreswangzh@gmail.com 2017/4/21.
 */
@Data
public class BaseMessage {

    // 消息ID
    @Id
    private Integer id;

    // 消息类型
    private String type;

    // 消息内容
    private String content;

    // 发送者
    private String sender;

    // 接受者 类型
    private String toType;

    // 接受者
    private String receiver;

    // 发送时间
    private Date date;

    public BaseMessage() {
        super();
    }

    public BaseMessage(Integer id, String sender, String receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "BaseMessage [id=" + id + ", type=" + type + ", content=" + content + ", sender=" + sender + ", toType="
                + toType + ", receiver=" + receiver + ", date=" + date + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
