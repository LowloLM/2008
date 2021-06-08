package com.fh.shop.api.testMq;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgInfo  implements Serializable {
    private String message;
    private String msgId;
    
}
