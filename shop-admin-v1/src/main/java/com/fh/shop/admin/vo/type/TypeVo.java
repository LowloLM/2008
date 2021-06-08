package com.fh.shop.admin.vo.type;

import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.admin.po.spec.Spec;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TypeVo implements Serializable {

    private List<Brand> brandList = new ArrayList<>();

    private List<Spec> specList = new ArrayList<>();

}
