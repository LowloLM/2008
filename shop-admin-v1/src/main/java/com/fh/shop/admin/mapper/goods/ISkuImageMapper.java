package com.fh.shop.admin.mapper.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.admin.po.goods.SkuImage;

import java.util.List;

public interface ISkuImageMapper extends BaseMapper<SkuImage> {

    void addBatch(List<SkuImage> skuImageList);
}
