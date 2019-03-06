package com.taotao.portal.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import com.taotao.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    @Value("${AD1_CATEGORY_ID}")
    private Long categoryId;

    @Value("${AD1_HEIGHT_B}")
    private Long AD1_HEIGHT_B;
    @Value("${AD1_HEIGHT}")
    private Long AD1_HEIGHT;
    @Value("${AD1_WIDTH}")
    private Long AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Long AD1_WIDTH_B;


    @RequestMapping("/index")
    public String index(Model model) {
        List<TbContent> tbContentList = contentService.getContentListByCatId(categoryId);
        List<Ad1Node> ad1NodeList = new ArrayList<>();
        for (TbContent tbContent : tbContentList) {
            Ad1Node ad1Node = new Ad1Node();
            ad1Node.setAlt(tbContent.getSubTitle());
            ad1Node.setHeight(AD1_HEIGHT + "");
            ad1Node.setHeightB(AD1_HEIGHT_B + "");
            ad1Node.setWidth(AD1_WIDTH + "");
            ad1Node.setWidthB(AD1_WIDTH_B + "");
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());

            ad1Node.setHref(tbContent.getUrl());
            ad1NodeList.add(ad1Node);
        }


        model.addAttribute("ad1", JsonUtils.objectToJson(ad1NodeList));

        return "index";
    }
}
