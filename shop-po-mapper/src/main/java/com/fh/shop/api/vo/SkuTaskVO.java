package com.fh.shop.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuTaskVO {
    private String skuName;
    private BigDecimal price;
    private int stock;
    private String brandName;
    private String cateName;

}
