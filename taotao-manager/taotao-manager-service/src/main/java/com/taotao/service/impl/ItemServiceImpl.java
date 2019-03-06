package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.EasyUIDataGridResult;
import com.taotao.common.TaotaoResult;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import com.taotao.util.IDUtils;
import com.taotao.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "topicDestination")
    private Destination destination;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;

    @Value("${ITEM_INFO_KEY_EXPIRE}")
    private Integer ITEM_INFO_KEY_EXPIRE;

    @Override
    public EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows) {
        page = null == page ? 1 : page;
        rows = null == rows ? 30 : rows;

        PageHelper.startPage(page, rows);
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        final EasyUIDataGridResult<TbItem> result = new EasyUIDataGridResult<>();
        result.setTotal((int) pageInfo.getTotal());
        result.setRows(pageInfo.getList());

        return result;
    }

    @Override
    public TbItem getItemById(Long itemId) {
        //从缓存中查，无则走数据库,ITEM_INFO:
        String baseKey = ITEM_INFO_KEY + itemId + ":BASE";

        final String tbItemJson = jedisClient.get(baseKey);
        TbItem tbItem = null;
        if (tbItemJson == null) {
            tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            jedisClient.set(baseKey, JsonUtils.objectToJson(tbItem));
        } else {
            tbItem = JsonUtils.jsonToPojo(tbItemJson, TbItem.class);
        }
        jedisClient.expire(baseKey, ITEM_INFO_KEY_EXPIRE);

        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        String descKey = ITEM_INFO_KEY + itemId + ":DESC";

        String tbItemDescJson = jedisClient.get(descKey);
        TbItemDesc tbItemDesc = null;
        if (tbItemDescJson == null) {
            tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
            jedisClient.set(descKey, JsonUtils.objectToJson(tbItemDesc));
        } else {
            System.out.println("使用缓存。。。");
            tbItemDesc = JsonUtils.jsonToPojo(tbItemDescJson, TbItemDesc.class);
        }
        jedisClient.expire(descKey, ITEM_INFO_KEY_EXPIRE);

        return tbItemDesc;
    }

    @Override
    public TaotaoResult saveItem(TbItem tbItem, String desc) {
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setCreated(new Date());
        tbItem.setStatus((byte) 1);
        tbItem.setUpdated(tbItem.getCreated());

        tbItemMapper.insert(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(tbItem.getCreated());
        tbItemDesc.setUpdated(tbItem.getUpdated());
        tbItemDescMapper.insertSelective(tbItemDesc);

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId + "");
            }
        });

        return TaotaoResult.ok();
    }


}
