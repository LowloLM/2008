package com.fh.shop.api.book.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookParam {
    private Long  id;
    private String bookName;
    private BigDecimal bookPrice;
    private String bookTime;
    private String bookAuthor;
}
