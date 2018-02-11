/**
 * MyMapper.java
 * cn.tw.mongo.util
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   ver1.0  2018年2月7日 		torreswang
 *
 * Copyright (c) 2018, b-i All Rights Reserved.
*/

package cn.tw.mongo.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * ClassName:MyMapper（Describe this Class）
 * 
 * @author torreswang
 * @version Ver 1.0
 * @Date 2018年2月7日 下午2:46:13
 * @see
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
