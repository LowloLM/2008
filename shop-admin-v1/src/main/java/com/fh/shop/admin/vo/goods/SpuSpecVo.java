package com.fh.shop.admin.vo.goods;

import com.fh.shop.admin.po.spec.SpecValue;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpuSpecVo implements Serializable {

    private String specName;

    private Long id;

    private List<SpecValue> specValueList = new ArrayList<>();

}
