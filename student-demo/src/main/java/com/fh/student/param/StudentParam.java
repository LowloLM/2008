package com.fh.student.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class StudentParam extends Page implements Serializable {
    private String stuName;
    private Integer minage;
    private Integer maxage;
}
