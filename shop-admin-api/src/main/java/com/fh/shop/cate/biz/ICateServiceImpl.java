package com.fh.shop.cate.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.cate.mapper.ICateMapper;
import com.fh.shop.cate.param.CateParam;
import com.fh.shop.cate.po.Cate;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cateService")
public class ICateServiceImpl implements ICateService {

    @Autowired
    private ICateMapper cateMapper;


    @Override
    public ServerResponse findAllList() {
        List<Cate> cateList = cateMapper.findAllList();
        return ServerResponse.success(cateList);
    }

    @Override
    public ServerResponse addCate(Cate cate) {
        cateMapper.addCate(cate);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteCate(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.error(ResponseEnum.CATE_IDS_IS_NULL);
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String s : idArr) {
            idList.add(Long.parseLong(s));
        }
        cateMapper.deleteCate(idList);
        return ServerResponse.success();
    }


    @Override
    public ServerResponse updateCate(CateParam cateParam) {
        Cate cate = cateParam.getCate();
        if (cate.getFatherId() == null) {
            cate.setFatherId(0L);
        }
        //更新分类信息
        cateMapper.updateCate(cate);
        //将当前分类下的所有子子孙孙的typeId以及typeName进行更新覆盖
        String[] idArr = cateParam.getIds().split(",");
        List<Long> idList = new ArrayList<>();
        for (String id : idArr) {
            idList.add(Long.parseLong(id));
        }
        cateParam.setIdList(idList);
        cateMapper.updateTypeInfo(cateParam);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse addFatherCate(Cate cate) {
        cate.setFatherId(0L);
        cateMapper.addFatherCate(cate);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findCateChilds(Long id) {
        //将当前id作为父id，寻找孩子
        QueryWrapper<Cate> cateQueryWrapper = new QueryWrapper<>();
        cateQueryWrapper.eq("fatherId",id);
        List<Cate> cates = cateMapper.selectList(cateQueryWrapper);
        return ServerResponse.success(cates);
    }

    @Override
    public ServerResponse queryList() {
        String cateList1 = RedisUtil.get("cateList");
        //先查询redis返回的值是否为空
        if (StringUtils.isEmpty(cateList1)){
            //为空则访问数据库
            List<Cate> cateList = cateMapper.selectList(null);
            //访问完数据库将我们查到的数据转换为json字符串保存进redis缓存中
            String cateListValue = JSON.toJSONString(cateList);
            //renturn返回我们第一次查询的集合
            RedisUtil.setEx("cateList",cateListValue,30);
            return ServerResponse.success(cateList);
        }else {
            //如果查到的值不为空则不去访问数据库
            List<Cate> cateList = JSON.parseArray(cateList1, Cate.class);
            return ServerResponse.success(cateList);
        }
    }


}
