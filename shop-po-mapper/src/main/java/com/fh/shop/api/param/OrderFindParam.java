package com.fh.shop.api.param;

import com.fh.shop.common.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderFindParam extends Page implements Serializable {

    private Long memberId;
    private String nickName;


}
