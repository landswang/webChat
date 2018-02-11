package cn.tw.chat.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import cn.tw.chat.Model.ChatMessage;
import cn.tw.mongo.model.BaseMessage;
import cn.tw.mongo.model.User;
import cn.tw.mongo.service.BaseMessageSevice;
import cn.tw.mongo.service.UserService;

/**
 * Created by torreswangzh@gmail.com 2017/4/21.
 */
@Controller
public class ChatController {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private BaseMessageSevice bmService;

    @Autowired
    private UserService userService;

    @MessageMapping("/all")
    public void all(Principal principal, String message) {
        // ChatMessage chatMessage = createMessage(principal.getName(), message,
        // null, 0);
        List<User> als = userService.selectAdminList();
        template.convertAndSend("/topic/notice", JSON.toJSONString(als));
        // template.convertAndSend("/topic/notice",
        // JSON.toJSONString(chatMessage));
    }

    @MessageMapping("/initserver")
    public void initserver(Principal principal, String message) {
        BaseMessage baseMessage = JSON.parseObject(message, BaseMessage.class);//
        // ChatMessage chatMessage = createMessage(principal.getName(), message,
        // null, 0);
        User user = userService.getByUsername(baseMessage.getSender());
        template.convertAndSendToUser(baseMessage.getReceiver(), "/topic/initserver", JSON.toJSONString(user));
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, String message) {
        BaseMessage baseMessage = JSON.parseObject(message, BaseMessage.class);//
        if (baseMessage.getSender() == null || ("").equals(baseMessage.getSender())) {
            baseMessage.setSender(principal.getName());
        }
        bmService.saveMessage(baseMessage);
        this.send(baseMessage);
    }

    private void send(BaseMessage message) {
        message.setDate(new Date());
        ChatMessage chatMessage = createMessage(message.getSender(), message.getContent(), null, 0);
        template.convertAndSendToUser(message.getReceiver(), "/topic/chat", JSON.toJSONString(chatMessage));
        template.convertAndSendToUser(message.getSender(), "/topic/chat", JSON.toJSONString(chatMessage));
        if (!("").equals(message.getToType()) && message.getToType().equals("ADMIN")) {
            template.convertAndSendToUser(message.getSender(), "/topic/chat",
                    JSON.toJSONString(createMessage(message.getReceiver(), "您好，请稍等客服为您解答。", null, 0)));
        }
    }

    @MessageMapping("/hismsg")
    public void hismsg(Principal principal, String message) {
        BaseMessage baseMessage = JSON.parseObject(message, BaseMessage.class);
        if (baseMessage.getSender() == null || ("").equals(baseMessage.getSender())) {
            baseMessage.setSender(principal.getName());
        }
        List<BaseMessage> bmds = bmService.getByHismsg(baseMessage);
        ChatMessage chatMessage = null;
        List<ChatMessage> cms = new ArrayList<ChatMessage>();
        for (BaseMessage bms : bmds) {
            chatMessage = createMessage(bms.getSender(), bms.getContent(), bms.getDate(), bms.getId());
            cms.add(chatMessage);
        } // 2b4f8689e49345918ea826a4a5105622
        Collections.reverse(cms);
        template.convertAndSendToUser(baseMessage.getSender(), "/topic/hismsg", JSON.toJSONString(cms));
    }

    private ChatMessage createMessage(String username, String message, Date date, Integer id) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        chatMessage.setUsername(username);
        User user = userService.getByUsername(username);
        chatMessage.setAvatar(user.getAvatar());
        chatMessage.setNickname(user.getNickname());
        chatMessage.setContent(message);
        chatMessage.setSendTime(date == null ? simpleDateFormat.format(new Date()) : simpleDateFormat.format(date));
        return chatMessage;
    }

}
