package com.taotao.search.controller;

import com.taotao.common.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(Model model, @RequestParam(value = "q", defaultValue = "*:*") String queryString,
                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                         @RequestParam(value = "rows", defaultValue = "60") Integer rows) throws Exception {

        System.out.println("query " + queryString);

        final SearchResult searchResult = searchService.search(queryString, page, rows);
        model.addAttribute("query", queryString);
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", searchResult.getPageCount());

        return "search";
    }

}
