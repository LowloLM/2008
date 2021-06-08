package com.fh.shop.admin.param;

import com.fh.shop.admin.po.goods.Spu;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpuStatusParam implements Serializable {

    private String flag;

    private Long spuId;

    private String status;

}
