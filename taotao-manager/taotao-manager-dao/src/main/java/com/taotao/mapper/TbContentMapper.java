package com.taotao.mapper;

import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * CREATE TABLE `tb_content` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT,
 * `category_id` bigint(20) NOT NULL COMMENT '内容类目ID',
 * `title` varchar(200) DEFAULT NULL COMMENT '内容标题',
 * `sub_title` varchar(100) DEFAULT NULL COMMENT '子标题',
 * `title_desc` varchar(500) DEFAULT NULL COMMENT '标题描述',
 * `url` varchar(500) DEFAULT NULL COMMENT '链接',
 * `pic` varchar(300) DEFAULT NULL COMMENT '图片绝对路径',
 * `pic2` varchar(300) DEFAULT NULL COMMENT '图片2',
 * `content` text COMMENT '内容',
 * `created` datetime DEFAULT NULL,
 * `updated` datetime DEFAULT NULL,
 * PRIMARY KEY (`id`),
 * KEY `category_id` (`category_id`), // 索引
 * KEY `updated` (`updated`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
 */

public interface TbContentMapper {
    long countByExample(TbContentExample example);

    int deleteByExample(TbContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbContent record);

    int insertSelective(TbContent record);

    List<TbContent> selectByExampleWithBLOBs(TbContentExample example);

    List<TbContent> selectByExample(TbContentExample example);

    TbContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByExampleWithBLOBs(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByExample(@Param("record") TbContent record, @Param("example") TbContentExample example);

    int updateByPrimaryKeySelective(TbContent record);

    int updateByPrimaryKeyWithBLOBs(TbContent record);

    int updateByPrimaryKey(TbContent record);
}