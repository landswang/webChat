package cn.tw.mongo.model;

import java.util.Date;
import java.util.Random;

import javax.persistence.Id;

import lombok.Data;
import lombok.ToString;

/**
 *
 * Created by torreswangzh@gmail.com 2017/4/22.
 */
@Data
@ToString
public class User {

    @Id
    private String id;

    // 用户名
    // @Indexed(unique = true)
    private String username;

    // 密码
    private String password;

    // 昵称
    private String nickname;

    // 头像
    private String avatar;

    // 登录时间
    private Date joinTime;

    private String status;

    public User() {
        super();
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = "/image/avatar/avatar" + new Random().nextInt(10) + ".jpg";
        this.joinTime = new Date();
    }

    public User(String username, String password, String nickname, String avatar) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

}
