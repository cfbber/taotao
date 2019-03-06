package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String getItemById(Model model, @PathVariable Long itemId) {
        final TbItem tbItem = itemService.getItemById(itemId);
        final Item item = new Item(tbItem);
        model.addAttribute("item", item);
        final TbItemDesc itemDescById = itemService.getItemDescById(itemId);
        model.addAttribute("itemDesc", itemDescById);

        return "item";
    }
}
