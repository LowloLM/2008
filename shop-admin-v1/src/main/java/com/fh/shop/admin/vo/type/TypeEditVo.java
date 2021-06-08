package com.fh.shop.admin.vo.type;

import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.admin.po.spec.Spec;
import com.fh.shop.admin.po.type.Attr;
import com.fh.shop.admin.po.type.Type;
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
