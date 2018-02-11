package cn.tw.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cn.tw.config.sercurity.UserPrincipal;
import cn.tw.mongo.model.User;
import cn.tw.mongo.service.RelationService;
import cn.tw.mongo.service.UserService;

/**
 *
 * Created by torreswangzh@qq.com on 2017/4/21.
 */
@Controller
public class PageController {

    @Autowired
    private RelationService relationService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String index(Model model, HttpSession session) {
        session.setMaxInactiveInterval(7200);//
        User user = (User) session.getAttribute("CURRENT_GUEST");// 0F6D36E36A17E885BE7247A60D739116
                                                                 // //3568EAA7E84A906CAFAC1D402CB92589
        if (user == null) {// fe04c7bb48b44cf2aff227f15fc2074f
                           // fe04c7bb48b44cf2aff227f15fc2074f
            int dd = (int) ((Math.random() * 9 + 1) * 1000);
            user = new User(UUID.randomUUID().toString().replaceAll("-", "") + "", "nopassw0rd", "游客" + dd,
                    "/image/avatar.jpg");
            userService.saveUser(user);
            session.setAttribute("CURRENT_GUEST", user);
        }
        model.addAttribute("cUUID", user.getUsername());
        System.out.println("------------------------------------------------------this is index");
        session.setAttribute("falg", 2);
        return "guest";
    }

    @GetMapping(value = "/vistor")
    public String vistor(Model model, HttpSession session) {
        // User user = new User(UUID.randomUUID().toString().replaceAll("-", "")
        // + "", "nopassw0rd", "游客",
        // "/image/avatar/avatar8.jpg");
        // userService.saveUser(user);
        // model.addAttribute("cUUID", user.getUsername());
        User user = (User) session.getAttribute("CURRENT_GUEST");
        if (user == null) {
            int dd = (int) ((Math.random() * 9 + 1) * 1000);
            user = new User(UUID.randomUUID().toString().replaceAll("-", "") + "", "nopassw0rd", "游客" + dd,
                    "/image/avatar.jpg");
            userService.saveUser(user);
            model.addAttribute("cUUID", user.getUsername());
            return "guest";
        }

        session.setAttribute("CURRENT_GUEST", user);
        model.addAttribute("_vistor", user);
        session.setAttribute("falg", 1);
        System.out.println("------------------------------------------------------this is vistor");
        return "vistor";
    }

    @GetMapping(value = "/chat")
    public String chat(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        model.addAttribute("user", userPrincipal);
        String username = userPrincipal.getUsername();
        List<User> friends = relationService.listFriends(username);
        model.addAttribute("friends", friends);
        System.out.println("------------------------------------------------------this is chat");
        return "chat";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }

    @GetMapping(value = "/fronter")
    public String fronter(HttpSession session, Model model) {
        model.addAttribute("flag", session.getAttribute("falg"));
        System.out.println(
                "------------------------------------------------------this falg is " + session.getAttribute("falg"));
        return "fronter";
    }

}
