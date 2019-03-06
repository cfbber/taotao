package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 显示商品详情页时，需要有多个image，相当于 vo
 */
public class Item extends TbItem {

    public Item(TbItem tbItem) {
        BeanUtils.copyProperties(tbItem,this);
    }

    public String[] getImages() {
        return StringUtils.isNoneBlank(super.getImage()) ?
                super.getImage().split(",") : null;
    }
}
