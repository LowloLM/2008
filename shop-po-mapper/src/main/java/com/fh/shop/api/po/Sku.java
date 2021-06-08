package com.fh.shop.api.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Sku implements Serializable {

    private Long id;

    private String skuName;

    private Long spuId;

    private BigDecimal price;

    private Integer stock;

    private String specInfo;

    private Long colorId;

    private String image;
    private Integer status;//0 下架 1 上架

    private Integer news;//0 非新品 1 新品

    private Integer recommend;//0 不推荐 1 推荐
    private Long sale;//销量

}
