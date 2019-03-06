package com.taotao.order.interceptor;

import com.taotao.common.TaotaoResult;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份拦截，打开订单前，判断登录
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Value("${SSO_URL}")
    private String SSO_URL;

    @Autowired
    private UserLoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        // 解决从订单页面访问拦截之后，直接跳回首页；
        // 实现：访问订单页面被拦截时，重定向时，将原URL作为参数redirect传递到 login页面，
        // 在PageController中将该url绑定到login.jsp,里面使用ajax，登录成功，windows.location 回订单页面

        final String redirectURL = SSO_URL + "/page/login" + "?" + "redirect=" + request.getRequestURL();
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(redirectURL);
            return false;
        }

        TaotaoResult result = loginService.getUserByToken(token);
        //如果过期，即在redis中的登录信息，已经被清理
        if (result.getStatus() != 200) {
            response.sendRedirect(redirectURL);
            return false;
        }
        request.setAttribute("USER_INFO", result.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
