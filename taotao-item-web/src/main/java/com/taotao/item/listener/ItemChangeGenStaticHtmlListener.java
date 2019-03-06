package com.taotao.item.listener;


import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听商品有修改时，生成静态html
 */
public class ItemChangeGenStaticHtmlListener implements MessageListener{

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try {
                Long itemId = Long.parseLong(textMessage.getText());
                final TbItem tbItem = itemService.getItemById(itemId);
                final Item item = new Item(tbItem);
                final TbItemDesc itemDescById = itemService.getItemDescById(itemId);

                Configuration configuration = freemarkerConfig.getConfiguration();
                final Template template = configuration.getTemplate("item.ftl");
                Writer writer = new FileWriter(new File("D:\\WorkSparce-web\\taotao\\taotao-item-web\\src\\main\\resources\\ftl\\html\\ "+item.getId()+".html"));
                Map<String,Object> model = new HashMap<>();
                item.setImage("a.jpg,b.jpg,c.jpg");
                model.put("item",item);
                model.put("itemDesc",itemDescById);

                template.process(model,writer);
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
