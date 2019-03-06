package com.taotao.cart.service.impl;

import com.taotao.cart.jedis.JedisClient;
import com.taotao.cart.service.CartService;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${TT_CART_REDIS_PRE_KEY}")
    private String TT_CART_REDIS_PRE_KEY;

    @Override
    public TaotaoResult addItemCart(TbItem tbItem, Integer num, Long userId) {
        final TbItem cartItem = queryItemByItemIdAndUserID(tbItem.getId(), userId);
        if (cartItem != null) {
            cartItem.setNum(cartItem.getNum() + num);
        } else {
            //购物车中只显示一张图片
            if (tbItem.getImage() != null) {
                tbItem.setImage(tbItem.getImage().split(",")[0]);
            }
            tbItem.setNum(num);
            //这应该对每个用户建立一个以用户ID为名的Map，做为购物车
            jedisClient.hset(TT_CART_REDIS_PRE_KEY + ":" + userId, tbItem.getId() + "", JsonUtils.objectToJson(tbItem));
        }

        return TaotaoResult.ok();
    }

    /**
     * 判断购物车中是否已有该商品，主要用来数量的更改
     *
     * @param itemId
     * @param userId
     * @return
     */
    private TbItem queryItemByItemIdAndUserID(Long itemId, Long userId) {
        final String tbItemJsonStr = jedisClient.hget(TT_CART_REDIS_PRE_KEY + ":" + userId, itemId + "");
        return StringUtils.isBlank(tbItemJsonStr) ? null :
                JsonUtils.jsonToPojo(tbItemJsonStr, TbItem.class);

    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        Map<String, String> map = jedisClient.hgetAll(TT_CART_REDIS_PRE_KEY + ":" + userId);
        List<TbItem> list = new ArrayList<>();
        for (String cartItem : map.values()) {
            list.add(JsonUtils.jsonToPojo(cartItem, TbItem.class));
        }
        return list;
    }

    @Override
    public TaotaoResult updateItemCartByItemId(Long userId, Long itemId, Integer num) {
        final TbItem tbItem = queryItemByItemIdAndUserID(itemId, userId);
        if (tbItem != null) {
            tbItem.setNum(num);
            jedisClient.hset(TT_CART_REDIS_PRE_KEY + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItemCartByItemId(Long userId, Long itemId) {
        jedisClient.hdel(TT_CART_REDIS_PRE_KEY + ":" + userId, itemId + "");
        return TaotaoResult.ok();
    }
}
