package com.fh.shop.admin.po.type;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttrValue implements Serializable {

    private Long id;

    private String attrValue;

    private Long attrId;

}
