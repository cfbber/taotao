package com.taotao.search.listener;

import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 这个监听器，是怎么调用的？怎么通信的？
 * 在taotao-search-service中使用,注册到spring container中
 */
public class ItemChangeMessageListener implements MessageListener {
    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            //根据获取到商品ID，查询数据库、更新索引库
            TextMessage textMessage = (TextMessage) message;
            try {
                searchService.updateSearchItemById(Long.parseLong(textMessage.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
