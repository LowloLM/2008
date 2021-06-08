package com.fh.shop.api.mq;

import lombok.Data;

@Data
public class PayMessage {

    private Long memberId;

    private String totalAmount;

    private String orderId;

}
