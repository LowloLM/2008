package com.fh.shop.type.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class TypeQueryParam extends Page implements Serializable {

    private String typeName;

}
