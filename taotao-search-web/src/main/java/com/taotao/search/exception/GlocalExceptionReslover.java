package com.taotao.search.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlocalExceptionReslover implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println(ex.getMessage());

        ex.printStackTrace();

        ModelAndView modelAndView = new ModelAndView("error/exception");
        modelAndView.addObject("message", "网络异常");

        return modelAndView;
    }
}
