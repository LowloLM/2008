package com.fh.shop.spec.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Spec implements Serializable {

    private long id;

    private String specName;

    private Integer sort;

}
