package com.fh.shop.type.vo;


import com.fh.shop.brand.po.Brand;
import com.fh.shop.spec.po.Spec;
import com.fh.shop.type.po.Attr;
import com.fh.shop.type.po.Type;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TypeEditVo implements Serializable {

    private List<Brand> brandList = new ArrayList<>();

    private List<Spec> specList = new ArrayList<>();

    private List<Attr> attrList = new ArrayList<>();

    private List<AttrVo> attrVoList = new ArrayList<>();

    private Type type = new Type();

    private List<Long> brandIdList = new ArrayList<>();

    private List<Long> specIdList = new ArrayList<>();

}
