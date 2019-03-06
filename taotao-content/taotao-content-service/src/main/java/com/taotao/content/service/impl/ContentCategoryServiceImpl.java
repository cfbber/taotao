package com.taotao.content.service.impl;

import com.taotao.common.EasyUITreeNode;
import com.taotao.common.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper mapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentId) {

        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        final List<TbContentCategory> tbContentCategories = mapper.selectByExample(example);

        List<EasyUITreeNode> list = new ArrayList<>();
        for (TbContentCategory tbContentCategory : tbContentCategories) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbContentCategory.getId());
            easyUITreeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            easyUITreeNode.setText(tbContentCategory.getName());
            list.add(easyUITreeNode);
        }

        return list;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        final TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);

        final TbContentCategory parent = mapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            parent.setIsParent(true);
            mapper.updateByPrimaryKey(parent);
        }


        mapper.insertSelective(tbContentCategory);

        return TaotaoResult.ok(tbContentCategory);
    }
}
