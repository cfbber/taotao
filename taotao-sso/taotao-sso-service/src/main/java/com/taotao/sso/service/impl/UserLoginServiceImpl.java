package com.taotao.sso.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;
import com.taotao.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private TbUserMapper mapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${EXPIRE_TIME}")
    private Integer EXPIRE_TIME;

    @Override
    public TaotaoResult login(String username, String password) {
        if (StringUtils.isAnyBlank(username, password)) {
            return TaotaoResult.build(400, "用户名或密码错误");
        }

        TbUserExample example = new TbUserExample();
        final TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        final List<TbUser> userList = mapper.selectByExample(example);

        if (userList == null || userList.isEmpty()) {
            return TaotaoResult.build(400, "用户名错误");
        }
        final TbUser tbUser = userList.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
            return TaotaoResult.build(400, "密码错误");
        }

        tbUser.setPassword(null);
        //为方便，直接在 token中加了 USER_INFO,实际应只有UUID
        String token = UUID.randomUUID().toString();
        jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(tbUser));
        jedisClient.expire(token, EXPIRE_TIME);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        if (token == null) return TaotaoResult.build(400, "token不能为null");
        String userJsonStr = jedisClient.get(USER_INFO + ":" + token);
        if (StringUtils.isBlank(userJsonStr)) {
            return TaotaoResult.build(400, "用户名会话已经过期");
        }
        jedisClient.expire(token, EXPIRE_TIME);
        return TaotaoResult.ok(JsonUtils.jsonToPojo(userJsonStr, TbUser.class));
    }
}
