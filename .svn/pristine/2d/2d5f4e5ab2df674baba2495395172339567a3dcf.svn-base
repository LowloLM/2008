package com.fh.shop.admin.biz.user;

import com.fh.shop.admin.mapper.user.IUserMapper;
import com.fh.shop.admin.param.UserQueryParam;
import com.fh.shop.admin.po.user.User;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("userService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    /***
     * 查询用户名是否存在
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        User user = userMapper.findUserByUserName(userName);
        return user;
    }

    /***
     * 查询
     * @param userQueryParam
     * @return
     */
    @Override
    public DataTableResult findList(UserQueryParam userQueryParam) {
        //获取总条数
        Long count = userMapper.findListCount(userQueryParam);
        //获取分页列表
        List<User> userList = userMapper.findPageList(userQueryParam);
        //组装数据
        return new DataTableResult(userQueryParam.getDraw(),count,count,userList);
    }

    /***
     * 新增
     * @param user
     */
    @Override
    public ServerResponse addUser(User user) {
        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_EMPTY);
        }
        if(!confirmPassword.equals(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NOT_SAME);
        }
        String salt = UUID.randomUUID().toString();
        user.setPassword(Md5Util.md5(Md5Util.md5(password)+","+salt));
        user.setSalt(salt);
        userMapper.insert(user);
        return ServerResponse.success();
    }

    /***
     * 删除
     * @param id
     */
    @Override
    public ServerResponse del(Long id) {
        userMapper.deleteById(id);
        return ServerResponse.success();
    }

    /***
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public ServerResponse deleteBatch(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            // 先ids转换为idList
            String[] idArr = ids.split(",");
            List<Long> idList = new ArrayList<>();
            for (String s : idArr) {
                idList.add(Long.parseLong(s));
            }
            // 删除记录
            userMapper.deleteBatchIds(idList);
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse findUser(Long id) {
        User user = userMapper.selectById(id);
        return ServerResponse.success(user);

    }

    @Override
    public ServerResponse update(User user, String rootRealPath) {
        // 业务
        String logo = user.getHeadImage();
        if (StringUtils.isNotEmpty(logo)) {
            // 上传了新图片
            // 删除旧图片
            String oldLogo = user.getOldLogo();
            // 删除图片，需要的是绝对路径
            String logoPath = rootRealPath + oldLogo;
            File file = new File(logoPath);
            if (file.exists()) {
                file.delete();
            }
        } else {
            // 没有上传新图片
            user.setHeadImage(user.getOldLogo());
        }
        userMapper.updateById(user);
        return ServerResponse.success(user);

    }

    @Override
    public ServerResponse importExcel(String filePath) {
        FileInputStream fis = null;
        try {
            //通过POI解析[读取]服务端硬盘上的Excel文件，获取里面的数据
            fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            //获取sheet
            XSSFSheet sheet = workbook.getSheetAt(2);
            //获取该sheet有数据内容的最后一行
            int lastRowNum = sheet.getLastRowNum();
            //循环读取每行的数据
            List<User> userList = new ArrayList<>();
            for (int i = 1; i <= lastRowNum; i++) {
                int count = 0;
                //读取行中的每行数据
                XSSFRow row = sheet.getRow(i);
                String userName = row.getCell(count++).getStringCellValue();
                String realName = row.getCell(count++).getStringCellValue();
                String sex = row.getCell(count++).getStringCellValue();
                Date birthday = row.getCell(count++).getDateCellValue();
                String phone = row.getCell(count++).getStringCellValue();
                String mail = row.getCell(count++).getStringCellValue();
                User user = new User();
                user.setLoginName(userName);
                user.setRealName(realName);
                user.setBirthday(birthday);
                user.setMail(mail);
                user.setPhone(phone);
                user.setSex(sex.equals("男")?1:0);
                userList.add(user);
            }
            //将数据插入到数据库
            userMapper.addBatch(userList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(null != fis){
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //删除垃圾文件
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
        }
        return ServerResponse.success();
    }

    @Override
    public void addTime(User user1) {
        userMapper.updateById(user1);
    }
}
