package com.fh.shop.spec.param;

import com.fh.shop.spec.po.Spec;
import com.fh.shop.spec.po.SpecValue;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpecAddParam implements Serializable {

    private Spec spec=new Spec();

    private List<SpecValue> specValueList=new ArrayList<>();


}
