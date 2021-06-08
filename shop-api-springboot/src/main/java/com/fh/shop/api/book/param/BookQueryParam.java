package com.fh.shop.api.book.param;

import com.fh.shop.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class BookQueryParam extends Page implements Serializable {

    @ApiModelProperty(value = "图书名")
    private String bookName;
    @ApiModelProperty(value = "图书最低价",example = "0")
    private BigDecimal minBookPrice;
    @ApiModelProperty(value = "图书最该价格",example = "0")
    private BigDecimal maxBookPrice;
    @ApiModelProperty(value = "最早出版时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @ApiModelProperty(value = "最晚的出版时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    @ApiModelProperty(value = "作者")
    private String bookAuthor;

}
