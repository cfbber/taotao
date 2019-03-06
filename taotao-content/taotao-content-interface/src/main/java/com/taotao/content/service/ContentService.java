package com.taotao.content.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容管理系统，相当于后台，为 taotao-manager-web 提供服务调用
 */
public interface ContentService {
    TaotaoResult saveContent(TbContent tbContent);
    List<TbContent> getContentListByCatId(Long categoryId);
}
