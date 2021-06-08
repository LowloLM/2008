package com.fh.shop.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@ApiModel

public class Page implements Serializable {

    @ApiModelProperty(value = "请求参数",example = "1",required = true)
    private Long draw;
    @ApiModelProperty(value = "开始位置",example = "1",required = true)
    private Long start;
    @ApiModelProperty(value = "每页数量",example = "10",required = true)
    private Long length;

}
