package com.fh.shop.admin.controller.user;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.user.IUserService;
import com.fh.shop.admin.controller.BaseController;
import com.fh.shop.admin.param.UserQueryParam;
import com.fh.shop.admin.po.user.User;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SystemConstant;
import com.fh.shop.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class UserController extends BaseController {

    @Resource(name = "userService")
    private IUserService userService;

    /***
     * 登录
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "登录")
    public ServerResponse login(User user, HttpSession session){
        //判断用户和密码非空
        String loginName = user.getLoginName();
        String password = user.getPassword();
        if(StringUtils.isEmpty(loginName)){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_EMPTY);
        }
        //判断用户名是否存在
        User userDB = userService.findUserByUserName(loginName);
        if(userDB == null){
            return ServerResponse.error(ResponseEnum.USERNAME_IS_ERROR);
        }
        //判断密码是否正确
        String salt = userDB.getSalt();
        if(!userDB.getPassword().equals(Md5Util.md5(password+","+salt))){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_ERROR);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        session.setAttribute("loginTime",sdf.format(userDB.getLoginTime()));
        User user1 = new User();
        if (userDB.getLoginCount()==null || !DateUtils.isSameDay(userDB.getNewTime(),new Date())){
            user1.setLoginCount(1);
        }else {
            user1.setLoginCount(userDB.getLoginCount()+1);
        }
        Long id = userDB.getId();
        user1.setNewTime(new Date());
        user1.setId(id);
        user1.setLoginTime(new Date());
        userService.addTime(user1);
        //登录成功，将用户信息存入session中，方便后续使用
        session.setAttribute(SystemConstant.CURR_USER,userDB);
        session.setAttribute("loginCount",user1.getLoginCount());
        return ServerResponse.success();
    }

    /***
     * 注销
     * @param session
     * @return
     */
    @RequestMapping(value = "/user/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){
        //清除session
        session.invalidate();
        //跳转页面
        return "redirect:/login2.jsp";
    }

    /***
     * 跳转到列表展示页面
     * @return
     */
    @RequestMapping(value = "/user/toList" ,method = RequestMethod.GET)
    public String toList(){
        return "/user/list";
    }

    /***
     * 查询
     * @param userQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/findList" ,method = RequestMethod.POST)
    public DataTableResult findList(UserQueryParam userQueryParam){
        return userService.findList(userQueryParam);
    }

    /***
     * 新增(弹框)
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/addUser" ,method = RequestMethod.POST)
    @LogInfo(info = "新增用户")
    public ServerResponse addUser(User user){
        return userService.addUser(user);
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/del" ,method = RequestMethod.POST)
    @LogInfo(info = "删除用户")
    public ServerResponse del(Long id){
        return userService.del(id);
    }

    /***
     * 批量删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/user/deleteBatch" ,method = RequestMethod.POST)
    @LogInfo(info = "批量删除用户")
    public ServerResponse deleteBatch(String ids){
        return userService.deleteBatch(ids);
    }

    //回显
    @ResponseBody
    @RequestMapping(value = "/user/findUser", method = RequestMethod.GET)
    public ServerResponse findUser(Long id) {
        return userService.findUser(id);
    }

    //修改
    @LogInfo(info = "修改用户")
    @ResponseBody
    @RequestMapping(value = "/user/update",method = RequestMethod.POST  )
    public ServerResponse update(User user, HttpServletRequest request){
        String rootRealPath = getRealPath("/", request);
        return userService.update(user,rootRealPath);
    }

    @ResponseBody
    @RequestMapping(value = "/user/importExcel",method = RequestMethod.POST)
    public ServerResponse importExcel(String filePath){
        return userService.importExcel(filePath);
    }

}
