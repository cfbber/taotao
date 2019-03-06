package com.taotao.search.service;

import com.taotao.common.SearchResult;
import com.taotao.common.TaotaoResult;

import java.io.IOException;

public interface SearchService {
    TaotaoResult importAllSearchItems() throws Exception;

    SearchResult search(String queryString,Integer page,Integer rows ) throws Exception;
    TaotaoResult updateSearchItemById(Long itemId) throws Exception;
}
