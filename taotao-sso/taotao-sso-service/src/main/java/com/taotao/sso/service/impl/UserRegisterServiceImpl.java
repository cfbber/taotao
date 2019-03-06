package com.taotao.sso.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private TbUserMapper mapper;

    @Override
    public TaotaoResult uesrRegister(TbUser user) {
        if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
            return TaotaoResult.build(400, "注册失败，请检验数据后再提交 ");
        }

        TaotaoResult result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return TaotaoResult.build(400, "注册失败，请检验数据后再提交 ");
        }

        if (StringUtils.isNotBlank(user.getPhone())) {
            result = checkData(user.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return TaotaoResult.build(400, "注册失败，请检验数据后再提交 ");
            }
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            result = checkData(user.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return TaotaoResult.build(400, "注册失败，请检验数据后再提交 ");
            }
        }

        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        mapper.insertSelective(user);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult checkData(String param, Integer type) {

        if (StringUtils.isEmpty(param)) {
            return TaotaoResult.ok(false);
        }

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return TaotaoResult.ok(false);
        }
        final List<TbUser> tbUsers = mapper.selectByExample(example);
        if (tbUsers == null || tbUsers.isEmpty()) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }
}
