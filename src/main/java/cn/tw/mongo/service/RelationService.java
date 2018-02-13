package cn.tw.mongo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tw.mongo.model.User;
import lombok.extern.log4j.Log4j;

/**
 * Created by torreswangzh@gmail.com 2017/4/23.
 */
@Service
@Log4j
public class RelationService {

    private final ConcurrentHashMap<String, List<String>> relations;

    public RelationService() {
        relations = new ConcurrentHashMap<>();
    }

    @Autowired
    private UserService userService;

    // 添加好友
    public boolean addFriend(String username, String friendName) {
        User user = userService.getByUsername(friendName);
        if (user == null) {
            // log.info("用户不存在：" + friendName);
            return false;
        }
        if (username.equals(friendName)) {
            // log.info("不能添加自己为好友：" + friendName);
            return false;
        }
        this.establishRelation(username, friendName);
        this.establishRelation(friendName, username);

        return true;

    }

    private void establishRelation(String username, String friendName) {
        List<String> friends = relations.get(username);
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(friendName);
        List<String> duplicateElements = getDuplicateElements(friends);
        if (duplicateElements.size() > 0) {
            friends.remove(friends.size() - 1);
        }
        relations.put(username, friends);
    }

    // 好友列表
    public List<User> listFriends(String username) {
        List<User> users = new ArrayList<>();
        List<String> friends = relations.get(username);
        if (friends != null) {
            for (String friend : friends) {
                User user = userService.getByUsername1(friend);
                if (user == null) {
                    user = userService.getByUsername(friend);
                }
                users.add(user);
            }
        }
        return users;
    }

    public static <E> List<E> getDuplicateElements(List<E> list) {
        return list.stream() // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, (a, b) -> a + b)) // 获得元素出现频率的
                                                                            // Map，键为元素，值为元素出现的次数
                .entrySet().stream() // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
                .map(entry -> entry.getKey()) // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList()); // 转化为 List
    }

    /**
     * 
     * returnUserNotBusy:(Describe the function of this method) @return @throws
     */
    public List<User> returnUserNotBusy(String uname) {
        List<User> al = listFriends(uname);
        if (al.size() >= 1) {
            return al;
        } else {
            List<User> als = userService.selectAdminList();
            if (als != null && als.size() > 0) {
                int[] usize = new int[als.size()];
                String[] unamesize = new String[als.size()];
                for (int i = 0; i < als.size(); i++) {
                    if (relations.get(als.get(i).getUsername()) != null) {
                        usize[i] = relations.get(als.get(i).getUsername()).size();
                    } else {
                        usize[i] = 0;
                    }
                    unamesize[i] = als.get(i).getUsername();
                }
                String temp = als.get(0).getUsername();
                // for (int i = 0; i < usize.length; i++) {
                // for (int j = i + 1; j < usize.length; j++) {
                // if (usize[i] > usize[j]) {
                // temp = unamesize[i];
                // unamesize[i] = unamesize[j];
                // unamesize[j] = temp;
                // }
                // }
                // }
                addFriend(uname, unamesize[0]);
                return listFriends(uname);
            }
            return null;
        }

    }

    /*
     * 平均分配
     */
    public Map<String, List<String>> allotOfAverage(List<String> users, List<String> tasks) {
        Map<String, List<String>> allot = new ConcurrentHashMap<String, List<String>>(); // 保存分配的信息
        if (users != null && users.size() > 0 && tasks != null && tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {
                int j = i % users.size();
                if (allot.containsKey(users.get(j))) {
                    List<String> list = allot.get(users.get(j));
                    list.add(tasks.get(i));
                    allot.put(users.get(j), list);
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add(tasks.get(i));
                    allot.put(users.get(j), list);
                }
            }
        }
        return allot;
    }

}
