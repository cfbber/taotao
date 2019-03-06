package com.taotao.sso.service;

import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserRegisterService {
    /**
     * 是否已经被注册
     *
     * @param param
     * @param type  参数类型，1/2/3表示不同的数据phone email
     * @return
     */
    TaotaoResult checkData(String param, Integer type);

    TaotaoResult uesrRegister(TbUser tbUser);

}
