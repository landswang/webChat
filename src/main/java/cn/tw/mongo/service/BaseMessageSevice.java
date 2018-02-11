package cn.tw.mongo.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tw.mongo.dao.BaseMessageDao;
import cn.tw.mongo.model.BaseMessage;
import cn.tw.mongo.model.User;

/**
 * User Service Created by torreswangzh@gmail.com 2017/4/22.
 */
@Service
public class BaseMessageSevice {

    private final ConcurrentHashMap<String, User> users;

    @Autowired
    private BaseMessageDao udao;

    public BaseMessageSevice() {
        users = new ConcurrentHashMap<>();
    }

    public boolean saveMessage(BaseMessage user) {
        udao.insert(user);
        return true;
    }

    public List<BaseMessage> getByHismsg(BaseMessage baseMsg) {

        return udao.getByHismsg(baseMsg);

    }
    // public User getByUsername(String username) {
    //
    // return udao.selectByUserName(username);
    //
    // }

}
