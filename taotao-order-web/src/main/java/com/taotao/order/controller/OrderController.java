package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @Value("${TT_CART_KEY}")
    private String TT_CART_KEY;

    /**
     * 订单直接从购物车中取。。。，有点不好，生产上不应该这样，应该是在数据库里
     */
    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request) {
        // 由于在拦截器对登录进行判断时，已经根据token获取过用户信息，不需要再一次根据 token需要用户信息，
        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        System.out.println("user:" + user);

//        final String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
//        final TaotaoResult result = loginService.getUserByToken(token);
//        if (result.getStatus() == 200) {
//
//            final TbUser user = (TbUser) result.getData();
//            final List<TbItem> cartList = cartService.getCartList(user.getId());
//            request.setAttribute("cartList",cartList);
//        }
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);

        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        final TaotaoResult result = orderService.createOrder(orderInfo);
        request.setAttribute("orderId", result.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        final DateTime dateTime = new DateTime();
        final DateTime dateTime1 = dateTime.plus(3);
        request.setAttribute("date", dateTime1.toString("yyyy-MM-dd"));
        return "success";
    }


}

