package com.fh.shop.admin.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpecQueryParam extends Page implements Serializable {

    private String specName;

}
