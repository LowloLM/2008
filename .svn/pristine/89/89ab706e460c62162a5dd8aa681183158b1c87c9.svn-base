package com.fh.shop.admin.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.param.UserQueryParam;
import com.fh.shop.admin.po.user.User;

import java.util.List;

public interface IUserMapper extends BaseMapper<User> {

    /***
     * 查询用户名是否存在
     * @param userName
     * @return
     */
    User findUserByUserName(String userName);

    /***
     * 查询总条数
     * @param userQueryParam
     * @return
     */
    Long findListCount(UserQueryParam userQueryParam);

    /***
     * 查询分页列表数据
     * @param userQueryParam
     * @return
     */
    List<User> findPageList(UserQueryParam userQueryParam);

    void addBatch(List<User> userList);
}
