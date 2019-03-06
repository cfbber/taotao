package com.taotao.controller;

import com.taotao.common.EasyUIDataGridResult;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;


    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getList(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        return itemService.getItemList(page, rows);
    }

    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    public TaotaoResult saveItem(final TbItem item, String desc) {
        final TaotaoResult taotaoResult = itemService.saveItem(item, desc);
        final Long itemId = item.getId();

        return TaotaoResult.ok();
    }


}
