package com.fh.shop.api.goods.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.po.Sku;
import com.fh.shop.api.vo.SkuTaskVO;
import com.fh.shop.api.vo.SkuVo;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("skuService")
@Transactional(rollbackFor = Exception.class)
public class ISkuServiceImpl implements ISkuService {

    @Autowired
    private ISkuMapper skuMapper;

    @Override
    public ServerResponse findRecommendNewsList() {
        String skuListInfo = RedisUtil.get("skuList");
        if(StringUtils.isNotEmpty(skuListInfo)){
            List<SkuVo> skuVoList = JSON.parseArray(skuListInfo, SkuVo.class);
            return ServerResponse.success(skuVoList);
        }
        List<Sku> skuList = skuMapper.findRecommendNewsList();
        List<SkuVo> skuVoList = skuList.stream().map(x -> {
            SkuVo skuVo = new SkuVo();
            skuVo.setId(x.getId());
            skuVo.setImage(x.getImage());
            skuVo.setPrice(x.getPrice().toString());
            skuVo.setSkuName(x.getSkuName());
            return skuVo;
        }).collect(Collectors.toList());
        String skuVoListJson = JSON.toJSONString(skuVoList);
        RedisUtil.set("skuList",skuVoListJson);

        return ServerResponse.success(skuVoList);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SkuTaskVO> skuTaskList(int stockLimit) {
        List<SkuTaskVO> skuTaskVOS= skuMapper.skuTaskList(stockLimit);
        return skuTaskVOS;
    }



}
