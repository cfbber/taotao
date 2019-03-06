package com.taotao.order.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${GEN_ORDER_ID_KEY}")
    private String GEN_ORDER_ID_KEY;

    @Value("${GEN_ORDER_ID_INIT}")
    private String GEN_ORDER_ID_INIT;

    @Value("${GEN_ORDER_ITEM_ID_KEY}")
    private String GEN_ORDER_ITEM_ID_KEY;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper itemMapper;

    @Autowired
    private TbOrderShippingMapper shippingMapper;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //通过redis incr生成订单id
        if (!jedisClient.exists(GEN_ORDER_ID_KEY)){
            jedisClient.set(GEN_ORDER_ID_KEY,GEN_ORDER_ID_INIT);
        }
        String orderId = jedisClient.incr(GEN_ORDER_ID_KEY).toString();
        //用户信息由controller设置

        orderInfo.setOrderId(orderId);
        orderInfo.setCreateTime(new Date());
        orderInfo.setPostFee("0");
        orderInfo.setStatus(1);
        orderInfo.setUpdateTime(orderInfo.getCreateTime());

        orderMapper.insert(orderInfo);

        // 与order共用自增key，不需要再判断
        final String itemId = jedisClient.incr(GEN_ORDER_ITEM_ID_KEY).toString();
        final List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(itemId);
            orderItem.setOrderId(orderId);
            itemMapper.insert(orderItem);
        }

        // 插入订单项表
        final TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setCreated(new Date());
        orderShipping.setOrderId(orderId);
        orderShipping.setUpdated(orderShipping.getCreated());
        shippingMapper.insert(orderShipping);

        return TaotaoResult.ok(orderId);
    }
}
