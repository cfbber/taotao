package com.taotao.service;

import com.taotao.common.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCatListByParentId(Long parentId);
}
