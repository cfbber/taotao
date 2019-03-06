package com.taotao.sso.controller;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserRegiseterController {
    @Autowired
    private UserRegisterService userRegisterService;

    @RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
        System.out.println(param + ":" + type);
        return userRegisterService.checkData(param, type);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult uesrRegister(TbUser tbUser) {
        return userRegisterService.uesrRegister(tbUser);
    }

}
