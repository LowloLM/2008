package com.fh.shop.admin.vo.goods;

import com.fh.shop.admin.po.brand.Brand;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpuVo implements Serializable {

    private List<Brand> brandList = new ArrayList<>();

    private List<SpuAttrVo> spuAttrVoList = new ArrayList<>();

    private List<SpuSpecVo> spuSpecVoList = new ArrayList<>();

}
