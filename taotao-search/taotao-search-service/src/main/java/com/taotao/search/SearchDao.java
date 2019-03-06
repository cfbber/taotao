package com.taotao.search;

import com.taotao.common.SearchItem;
import com.taotao.common.SearchResult;
import com.taotao.common.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemMapper searchItemMapper;

    public SearchResult search(SolrQuery solrQuery) throws Exception {
        SearchResult searchResult = new SearchResult();

        QueryResponse response = solrServer.query(solrQuery);
        final SolrDocumentList results = response.getResults();
        searchResult.setRecordCount(results.getNumFound());
        List<SearchItem> itemList = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument solrDocument : results) {
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name(solrDocument.get("item_category_name").toString());
            searchItem.setId(Long.parseLong(solrDocument.get("id").toString()));
            searchItem.setImage(solrDocument.get("item_image").toString());
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSell_point(solrDocument.get("item_sell_point").toString());
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String highLightStr = null != list && !list.isEmpty() ? list.get(0) :
                    solrDocument.get("item_title").toString();
            searchItem.setTitle(highLightStr);
            itemList.add(searchItem);
        }
        searchResult.setItemList(itemList);
        return searchResult;
    }

    public TaotaoResult updateSearchItemById(Long itemId) throws Exception{
        SearchItem searchItem = searchItemMapper.getSearchItemById(itemId);
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", searchItem.getId().toString());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("price", searchItem.getPrice());

        solrServer.commit();

        return TaotaoResult.ok();
    }

}

