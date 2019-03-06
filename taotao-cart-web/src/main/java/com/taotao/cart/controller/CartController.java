package com.taotao.cart.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.service.ItemService;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.CookieUtils;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class CartController {

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @Value("${TT_CART_KEY}")
    private String TT_CART_KEY;

    @Autowired
    private CartService cartService;
    @Autowired
    private UserLoginService loginService;
    @Autowired
    private ItemService itemService;

    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable Long itemId, @RequestParam("num") Integer num) {
        final String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        System.out.println(TT_TOKEN_KEY + ":" + token);

        final TbItem tbItem = itemService.getItemById(itemId);


        TaotaoResult result = loginService.getUserByToken(token);
        //正常登录
        if (result.getStatus() == 200) {
            TbUser user = (TbUser) result.getData();
            cartService.addItemCart(tbItem, num, user.getId());
        } else {
            //未登录时，加到本地cookie
            final List<TbItem> cookieCartList = getCookieCartList(request);
            // 整体逻辑，判断有则+num，无则add，
            // 应该用List.contain()要好点，避免for循环
            boolean isContains = false;
            for (TbItem item : cookieCartList) {
                if (item.getId().equals(tbItem.getId())) {
                    item.setNum(item.getNum() + num);
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                tbItem.setNum(num);
                if (tbItem.getImage() != null) {
                    tbItem.setImage(tbItem.getImage().split(",")[0]);
                }
                cookieCartList.add(tbItem);
            }
            CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cookieCartList),
                    7 * 24 * 3600, true);

        }
        return "cartSuccess";
    }

    public List<TbItem> getCookieCartList(HttpServletRequest request) {
        final String value = CookieUtils.getCookieValue(request, TT_CART_KEY,true);
        return StringUtils.isNotBlank(value) ?
                JsonUtils.jsonToList(value, TbItem.class) : new ArrayList<TbItem>();
    }

    @RequestMapping("/cart/cart")
    public String getCartList(HttpServletRequest request, Model model) {
        final String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        final TaotaoResult userResult = loginService.getUserByToken(token);
        List<TbItem> cartList = null;
        if (userResult.getStatus() == 200) {
            cartList = cartService.getCartList(((TbUser) userResult.getData()).getId());
            System.out.println("购物车里的内容：" + cartList);
        } else {
            cartList = getCookieCartList(request);
        }
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemCartByItemId(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
                                               HttpServletRequest request, HttpServletResponse response) {
        // 看整体逻辑只更新，并没有对更新后的结果进行返回
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        TaotaoResult userResult = loginService.getUserByToken(token);
        if (userResult.getStatus() == 200) {
            cartService.updateItemCartByItemId(((TbUser) userResult.getData()).getId(), itemId, num);
        } else {
            updateCookieCartItem(itemId, num, request, response);
        }
        return TaotaoResult.ok();
    }

    private void updateCookieCartItem(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        final List<TbItem> cookieCartList = getCookieCartList(request);
        final TbItem tbItem = itemService.getItemById(itemId);
        for (TbItem item : cookieCartList) {
            if (item.getId().equals(tbItem.getId())) {
                item.setNum( num);
                break;
            }
        }
        //不存在，不处理
        CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cookieCartList),
                7 * 24 * 3600, true);
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItemByItemId(@PathVariable Long itemId, HttpServletRequest request,
                                         HttpServletResponse response) {
        final String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        final TaotaoResult userResult = loginService.getUserByToken(token);
        if (userResult.getStatus() == 200) {
            cartService.deleteItemCartByItemId(((TbUser) userResult.getData()).getId(), itemId);
        } else {
            deleteCookieItemCartByItemId(itemId, request, response);
        }
        return "redirect:/cart/cart.html";
    }

    private void deleteCookieItemCartByItemId(Long itemId, HttpServletRequest request,
                                              HttpServletResponse response) {
        final List<TbItem> cookieCartList = getCookieCartList(request);
        final Iterator<TbItem> iterator = cookieCartList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(itemId)) {
                iterator.remove();
                break;
            }
        }
        CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cookieCartList),
                7 * 24 * 3600, true);
    }
}
