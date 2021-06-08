package com.fh.shop.api.reclpient.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Recipient  implements Serializable {
    private Long id;
    private Long memberId;
    private String recipientName;
    private String  phone;
    private String address;
    private String  status;

}
