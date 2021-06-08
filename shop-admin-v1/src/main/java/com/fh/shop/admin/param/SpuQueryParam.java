package com.fh.shop.admin.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SpuQueryParam extends Page implements Serializable {

    private String spuName;

    private BigDecimal startPrice;

    private BigDecimal endPrice;

    private Integer startStock;

    private Integer endStock;

    private Long brandId;

    private Long cate1;

    private Long cate2;

    private Long cate3;

}
