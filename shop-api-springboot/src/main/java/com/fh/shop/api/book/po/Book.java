package com.fh.shop.api.book.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class Book {
    @ApiModelProperty(value = "图书id",example = "0",hidden = true)
    private Long  id;
    @ApiModelProperty(value = "图书名字")
    private String bookName;
    @ApiModelProperty(value = "图书价格",example = "0")
    private BigDecimal bookPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "图书出版日期")
    private Date bookTime;
    @ApiModelProperty(value = "图书作者")
    private String bookAuthor;
}
