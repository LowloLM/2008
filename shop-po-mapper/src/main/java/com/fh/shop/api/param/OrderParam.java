package com.fh.shop.api.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderParam implements Serializable {
    private Integer payType;
    private Long recipientId;
   // @ApiModelProperty(hidden = true)
    // private Long memberId;

}
