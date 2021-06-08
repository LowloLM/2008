package com.fh.shop.admin.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class BrandQueryParam extends Page implements Serializable {

    private String brandName;

}
