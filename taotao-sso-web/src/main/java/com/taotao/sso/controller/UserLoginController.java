package com.taotao.sso.controller;

import com.taotao.common.TaotaoResult;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserLoginController {
    @Autowired
    private UserLoginService loginService;

    @RequestMapping(value = "/user/login", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public TaotaoResult login(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password) {
        TaotaoResult result = loginService.login(username, password);
        if (result.getStatus() == 200) {
            String token = (String) result.getData();
            CookieUtils.setCookie(request, response, "TT_TOKEN", token);
//            response.addCookie(new Cookie("sessionID", token));
            // set用户token
            request.setAttribute("USER_INFO",result.getData());
        }
        return result;
    }

    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public TaotaoResult getUserByToken(@PathVariable String token) {
        return loginService.getUserByToken(token);
    }

}
