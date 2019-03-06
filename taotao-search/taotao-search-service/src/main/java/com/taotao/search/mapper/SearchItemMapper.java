package com.taotao.search.mapper;

import com.taotao.common.SearchItem;

import java.util.List;

public interface SearchItemMapper {

    List<SearchItem> getSearchItemList();

    SearchItem getSearchItemById(long itemId);
}
