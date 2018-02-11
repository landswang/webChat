package cn.tw.mongo.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tw.mongo.dao.UserDao;
import cn.tw.mongo.model.User;

/**
 * User Service Created by torreswangzh@gmail.com 2017/4/22.
 */
@Service
public class UserService {

    private final ConcurrentHashMap<String, User> users;

    @Autowired
    private UserDao udao;

    public UserService() {
        users = new ConcurrentHashMap<>();
    }

    public boolean addUser(User user) {
        boolean isExist = users.containsKey(user.getUsername());
        if (isExist) {
            return false;
        }
        udao.insert(user);
        return true;
    }

    public boolean saveUser(User user) {
        boolean isExist = users.containsKey(user.getUsername());
        if (isExist) {
            return false;
        }
        users.put(user.getUsername(), user);
        return true;
    }

    public User getByUsername(String username) {
        User user = udao.selectByUserName(username);
        if (user == null) {
            user = this.getByUsername1(username);
        }
        return user;
    }

    public User getByUsername1(String username) {
        return users.get(username);
    }

    public List<User> selectAdminList() {
        return udao.selectByUserStatus(1);
    }
}
