package com.fh.shop.api.mq;

import lombok.Data;

/**
 * 死信队列的发邮件
 * 邮箱的参数
 */
@Data
public class MailMessage {

    private String to;

    private String title;

    private String conect;

    private String msgId;
}
