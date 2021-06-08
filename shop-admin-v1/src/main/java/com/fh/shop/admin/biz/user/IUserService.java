package com.fh.shop.admin.biz.user;

import com.fh.shop.admin.param.UserQueryParam;
import com.fh.shop.admin.po.user.User;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface IUserService {

    /***
     * 查询用户名是否存在
     * @param userName
     * @return
     */
    User findUserByUserName(String userName);

    /***
     * 查询
     * @param userQueryParam
     * @return
     */
    DataTableResult findList(UserQueryParam userQueryParam);

    /***
     * 新增
     * @param user
     */
    ServerResponse addUser(User user);

    /***
     * 删除
     * @param id
     */
    ServerResponse del(Long id);

    /***
     * 批量删除
     * @param ids
     * @return
     */
    ServerResponse deleteBatch(String ids);

    ServerResponse findUser(Long id);

    ServerResponse update(User user, String rootRealPath);

    ServerResponse importExcel(String filePath);

    void addTime(User user1);
}
