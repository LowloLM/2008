package com.fh.shop.admin.vo.goods;

import com.fh.shop.admin.po.goods.SkuImage;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SkuImageVo implements Serializable {

    private String colorName;

    private Long colorId;

    private List<SkuImage> skuImageList = new ArrayList<>();

}
