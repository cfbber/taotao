package com.taotao.service;

import com.taotao.common.EasyUIDataGridResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 *
 * 查询商品详情信息,这个跟 taotao-content {@link ContentService }有什么区别？
 * 表tb_item ,tb_content的区别，不少内容重叠，一个应该用来在商品详情页展示、后台添加商品
 * 另一个，应该只用在订单上
 *
 */

public interface ItemService {
    EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows);

    TaotaoResult saveItem(TbItem tbItem, String desc);

    TbItem getItemById(Long itemId);
    TbItemDesc getItemDescById(Long itemId);
}
