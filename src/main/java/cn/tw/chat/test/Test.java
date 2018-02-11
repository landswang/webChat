/**
 * Test.java
 * cn.tw.chat.test
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月8日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package cn.tw.chat.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.tw.mongo.model.BaseMessage;
import cn.tw.mongo.service.BaseMessageSevice;

/**
 * ClassName:Test（Describe this Class）
 * 
 * @author torreswang
 * @version Ver 1.0
 * @Date 2018年2月8日 上午11:21:13
 * @see
 */
public class Test {
    @Autowired
    public static BaseMessageSevice bmd = new BaseMessageSevice();

    public static void main(String[] args) {
        List<BaseMessage> bmds = bmd.getByHismsg(new BaseMessage(50, "test", "admin"));
        for (BaseMessage baseMessage : bmds) {
            System.out.println(baseMessage);
        }
    }

}
