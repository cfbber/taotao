package com.taotao.search.service.impl;

import com.taotao.common.SearchItem;
import com.taotao.common.SearchResult;
import com.taotao.common.TaotaoResult;
import com.taotao.search.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchDao searchDao;

    @Override
    public TaotaoResult importAllSearchItems() throws Exception {

        final List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();

        for (SearchItem searchItem : searchItemList) {
            SolrInputDocument solrInputFields = new SolrInputDocument();
            solrInputFields.addField("id", searchItem.getId().toString());
            solrInputFields.addField("item_title", searchItem.getTitle());
            solrInputFields.addField("item_sell_point", searchItem.getSell_point());
            solrInputFields.addField("item_price", searchItem.getPrice());
            solrInputFields.addField("item_image", searchItem.getImage());
            solrInputFields.addField("item_category_name", searchItem.getCategory_name());
            solrInputFields.addField("price", searchItem.getPrice());
            try {
                solrServer.add(solrInputFields);
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            solrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateSearchItemById(Long itemId) throws Exception {
        searchDao.updateSearchItemById(itemId);
        return TaotaoResult.ok();
    }

    @Override
    public SearchResult search(String queryString,
                               Integer page,
                               Integer rows) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(queryString);
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        solrQuery.set("df", "item_keywords");
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        solrQuery.addHighlightField("item_title");

        SearchResult searchResult = searchDao.search(solrQuery);
        long pageCount = (long) Math.ceil(searchResult.getRecordCount() / rows);
        searchResult.setPageCount(pageCount);
        return searchResult;
    }
}
