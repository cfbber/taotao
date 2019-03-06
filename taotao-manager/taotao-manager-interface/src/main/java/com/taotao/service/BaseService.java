package com.taotao.service;

import java.util.List;

public interface BaseService<T> {
    T quyeryById(Long id);
    List<T> queryAll();

    Integer queryCountByWhere(T t );

    List<T> queryListByWhere(T t);

    List<T> queryByPage(Integer page,Integer rows);

    T queryOne(T t);

    void save(T t);

    void saveSelective(T t);

    void updateById(T t);

    void updateBySelective(T  t);

    void deleteByid(Long id);

    void deleteByIds(List<Object> objs);

}
