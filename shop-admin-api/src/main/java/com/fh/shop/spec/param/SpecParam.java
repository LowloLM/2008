package com.fh.shop.spec.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpecParam implements Serializable {

    private Long id;

    private String specNames;

    private String specNameSorts;

    private String specValueInfos;

}
