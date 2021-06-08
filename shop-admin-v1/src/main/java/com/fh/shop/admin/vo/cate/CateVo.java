package com.fh.shop.admin.vo.cate;

import com.fh.shop.admin.po.cate.Cate;
import com.fh.shop.admin.po.type.Type;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CateVo implements Serializable {

    private Cate cate = new Cate();

    private List<Type> typeList = new ArrayList<>();

}
