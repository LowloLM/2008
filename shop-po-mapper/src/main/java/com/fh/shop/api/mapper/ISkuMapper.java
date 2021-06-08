package com.fh.shop.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.param.SkuTask;
import com.fh.shop.api.po.Sku;
import com.fh.shop.api.vo.CartItemVO;
import com.fh.shop.api.vo.SkuTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISkuMapper extends BaseMapper<Sku> {

    List<Sku> findRecommendNewsList();


    List<SkuTaskVO> skuTaskList(int stockLimit);

    int updateStock(CartItemVO cartItemVO);

    void updateSkuStock(@Param("skuId") Long skuId,@Param("count") Long count);

    void updateSalesVolume(@Param("skuId")Long skuId,@Param("skuCount")Long skuCount);
}
