package com.fh.shop.common;

import lombok.Getter;

/***
 * 枚举
 */
@Getter
public enum ResponseEnum {
    TOKEN_IS_ERROR(9999, "重复发送"),

   PAY_ORDER_ID_IS_ERROR(8100, "支付的订单不存在"),

   PAY_ORDER_TYPE_IS_ERROR(8101, "支付的订单状态错误"),

    ORDER_RECIPIENT_IS_NULL(8000, "订单状态错误"),

    CART_BATCH_DELETE_NO_SELECT(7005, "请选择要删除的商品"),

    ORDER_RECIPENT_IS_NOT(7006, "收件人为空"),

    CART_IS_ERROR(7004, "操作违规"),

    CART_SKU_COUNT_LIMIT(7003, "商品限购"),

    CART_SKU_STOCK_IS_ERROR(7002, "商品库存不足"),

    CART_SKU_IS_DOWN(7001, "商品下架了"),

    CART_SKU_IS_NULL(7000, "商品不存在"),

    DATA_IS_INCOMPLETE(5000,"数据填写不完整"),

    DATA_PASSWORD_IS_INCOMPLETE(5001,"密码格式不正确"),

    DATA_SMS_IS_INCOMPLETE(5002,"验证码格式不正确"),

    DATA_MEMBER_IS_INCOMPLETE(5003,"会员格式不正确"),

    MEMBER_IS_EXIST(5004,"用户已存在"),

    EMAIL_IS_EXIST(5005,"邮箱已存在"),

    EMAIL_IS_NO_EXIST(5055,"邮箱不存在"),

    EMAIL_IS_NOT(5006,"邮箱格式错误"),

    PHONE_IS_XIST(5007,"手机号已注册"),

    PHONE_IS_NOT(5008,"手机号码格式错误"),

    EMAIL_MEMBERNAME_IS_NOT(5009,"用户名和密码不匹配"),

    MEMBER_IS_NOT(5009,"信息为空"),

    EMAILCODE_IS_NOT(5011,""),

    CODE_IS_NOT(5012,"验证码错误"),

    TOKEN_IS_MISS(5100,"头部信息丢失"),

    TOKEN_IS_NOT_FULL(5101,"头部信息不完整"),

    TOKEN_IS_FALL(5102,"验签失败"),

    PASSWORD_IS_EMPT(5103,"登陆超时"),

    OUT_PASSWORD_IS_EMPT(5104,"旧密码错误"),

    State_IS_NOT(5104,"未授权，请激活"),

    SMS_CODE_IS_ERROR(4001,"发送失败"),

    SMS_IS_NOT(4000,"手机号不能为空"),

    CATE_IDS_IS_NULL(3000,"请选择要删除的分类"),

    TYPE_INFO_IS_NULL(2001,"类型信息为空"),

    SPEC_INFO_IS_NULL(2000,"规格信息为空"),

    USERNAME_IS_EMPTY(1000,"用户名为空"),

    PASSWORD_IS_ERROR(1003,"密码错误"),

    PASSWORD_IS_NOT_SAME(1004,"密码不一致"),

    USERNAME_IS_ERROR(1002,"用户名错误"),

    PASSWORD_IS_EMPTY(1001,"密码为空");

    private Integer code;

    private String msg;

    private ResponseEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
