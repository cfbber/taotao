package com.taotao.content.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult saveContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());

        tbContentMapper.insert(tbContent);
        jedisClient.hdel("category"+tbContent.getCategoryId());

        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {
        String redisKey = "category" + categoryId;
        if (jedisClient.exists(redisKey)){
            return  JsonUtils.jsonToList(jedisClient.get(redisKey),TbContent.class);
        }

        final TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);

        final List<TbContent> tbContents = tbContentMapper.selectByExample(example);
        String toJson = JsonUtils.objectToJson(tbContents);
        jedisClient.set(redisKey, toJson);

        return tbContents;
    }
}
