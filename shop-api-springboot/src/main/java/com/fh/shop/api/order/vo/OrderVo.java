package com.fh.shop.api.order.vo;

        import com.fasterxml.jackson.annotation.JsonFormat;
        import lombok.Data;
        import org.springframework.format.annotation.DateTimeFormat;

        import java.io.Serializable;
        import java.math.BigDecimal;
        import java.util.Date;

@Data
public class OrderVo implements Serializable {

    private String id;

    private String totalPrice;

    private Long totalCount;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    private String recipientName;

    private String recipientAddr;

    private String recipientPhone;

    private Integer payType;

    private String memberName;

}
