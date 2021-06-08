package com.fh.shop.admin.po.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Spu implements Serializable {

    private Long id;

    private Long brandId;

    private Long cate1;

    private Long cate2;

    private Long cate3;

    private String attrInfo;

    private String specInfo;

    private Integer stock;

    private BigDecimal price;

    private String spuName;

    private String brandName;

    private String cateName;

    private Integer status;//0 下架 1 上架

    private Integer news;//0 非新品 1 新品

    private Integer recommend;//0 不推荐 1 推荐

}
