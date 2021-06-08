package com.fh.shop.cate.param;

import com.fh.shop.cate.po.Cate;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CateParam implements Serializable {

    private Cate cate = new Cate();

    private String ids;

    private List<Long> idList = new ArrayList<>();

}
