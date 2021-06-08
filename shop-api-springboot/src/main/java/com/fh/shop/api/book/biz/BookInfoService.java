package com.fh.shop.api.book.biz;

import com.fh.shop.api.book.mapper.IBookMapper;
import com.fh.shop.api.book.param.BookParam;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.common.IResponse;
import com.fh.shop.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("responseService")
@Transactional(rollbackFor = Exception.class)
public class BookInfoService implements IBookInfoService {

    @Autowired
    private IBookMapper responsemapper;

    @Override
    public IResponse add(BookParam bookParam) {
        Book book = new Book();
        book.setBookName(bookParam.getBookName());
        book.setBookPrice(bookParam.getBookPrice());
        book.setBookAuthor(bookParam.getBookAuthor());
        book.setBookTime(DateUtil.str2Date(bookParam.getBookTime(),DateUtil.Y_M_D));
        responsemapper.insert(book);
        return IResponse.success();
    }
}
