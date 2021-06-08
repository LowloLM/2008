package com.fh.shop.api.book.biz;

import com.fh.shop.api.book.param.BookParam;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.common.IResponse;

public interface IBookInfoService {
    IResponse add(BookParam bookParam);
}
