package cn.tw.config.sercurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.tw.mongo.model.User;
import cn.tw.mongo.service.UserService;

/**
 *
 * Created by torreswangzh@gmail.com 2017/4/22.
 */
@Service
public class AnyUserDetailsService implements UserDetailsService {
    private final static String ROLE_TAG = "ROLE_USER";

    @Autowired
    private UserService userService;

    List<User> cUsers = new ArrayList<User>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername1(username);
        if (user == null) {
            user = userService.getByUsername(username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_TAG));
        // 用户认证（用户名，密码，权限）
        UserPrincipal userPrincipal = new UserPrincipal(username, user.getPassword(), authorities);
        userPrincipal.setNickname(user.getNickname());
        userPrincipal.setAvatar(user.getAvatar());
        // checkStatus("admin");
        return userPrincipal;

    }

    public void checkStatus(String accName) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(accName)) {
            cUsers.add(new User(accName, "", ""));
            System.out.println("LOGGED IN");
        } else {
            System.out.println("NOT LOGGED IN");
        }
    }

}
