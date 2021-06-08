package com.fh.shop.type.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttrValue implements Serializable {

    private Long id;

    private String attrValue;

    private Long attrId;

}
