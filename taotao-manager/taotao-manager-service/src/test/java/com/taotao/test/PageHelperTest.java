package com.taotao.test;


import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-dao.xml")
public class PageHelperTest {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Test
    public void t1() {
        PageHelper.startPage(1, 3);
        final List<TbContent> tbContents = tbContentMapper.selectByExample(null);
        final List<TbContent> tbContents2 = tbContentMapper.selectByExample(null);
        System.out.println(tbContents.size());
        System.out.println(tbContents2.size());
    }
}
