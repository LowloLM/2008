package com.fh.shop.type.vo;


import com.fh.shop.brand.po.Brand;
import com.fh.shop.spec.po.Spec;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TypeVo implements Serializable {

    private List<Brand> brandList = new ArrayList<>();

    private List<Spec> specList = new ArrayList<>();

}
