/**
 * UserDao.java
 * cn.tw.mongo.dao
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月7日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package cn.tw.mongo.dao;

import java.util.List;

import cn.tw.mongo.model.BaseMessage;
import cn.tw.mongo.util.MyMapper;

/**
 * ClassName:UserDao（Describe this Class）
 * 
 * @author torreswang
 * @version Ver 1.0
 * @Date 2018年2月7日 下午2:54:18 @see;
 */
public interface BaseMessageDao extends MyMapper<BaseMessage> {

    public List<BaseMessage> getByHismsg(BaseMessage baseMsg);
}
