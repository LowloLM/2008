package com.fh.shop.type.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Attr implements Serializable {

    private Long id;

    private String attrName;

    private Long typeId;

}
