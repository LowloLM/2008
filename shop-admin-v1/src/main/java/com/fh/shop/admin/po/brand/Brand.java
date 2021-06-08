package com.fh.shop.admin.po.brand;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/***
 * 品牌
 */
@Data
public class Brand implements Serializable {

    private Long id;

    private String brandName;

    private String logo;

    @TableField(exist = false)
    private String oldLogo;

    private Integer createYear;

}
