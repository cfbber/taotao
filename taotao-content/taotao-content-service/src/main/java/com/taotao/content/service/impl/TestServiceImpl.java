package com.taotao.content.service.impl;

import com.taotao.content.service.TestService;
import com.taotao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public String testQuery() {
        return testMapper.testQuery();
    }
}
