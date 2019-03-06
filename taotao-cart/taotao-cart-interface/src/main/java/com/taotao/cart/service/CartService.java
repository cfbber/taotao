package com.taotao.cart.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;

import java.util.List;

public interface CartService {
    /**
     * 添加购物车
     *
     * @param tbItem
     * @param num
     * @param userId
     * @return
     */
    TaotaoResult addItemCart(TbItem tbItem, Integer num, Long userId);

    List<TbItem> getCartList(Long userId);

    /**
     * 修改商品数量
     *
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num);

    TaotaoResult deleteItemCartByItemId(Long userId, Long itemId);

}
