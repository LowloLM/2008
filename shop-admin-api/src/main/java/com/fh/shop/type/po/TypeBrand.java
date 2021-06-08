package com.fh.shop.type.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class TypeBrand implements Serializable {

    private Long id;

    private Long typeId;

    private Long brandId;

}
