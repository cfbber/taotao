package com.taotao.content.service;

import com.taotao.common.EasyUITreeNode;
import com.taotao.common.TaotaoResult;

import java.util.List;

/**
 * 内容分类管理，即类目管理
 */
public interface ContentCategoryService {

    List<EasyUITreeNode>  getContentCategoryList(Long parentId);
    TaotaoResult createContentCategory(Long parentId,String name);
}
