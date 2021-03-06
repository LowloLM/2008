package com.fh.shop.api.book.biz;


import com.fh.shop.api.book.mapper.IBookMapper;
import com.fh.shop.api.book.param.BookQueryParam;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("bookService")
public class IBookServiceImpl implements IBookService {

    @Autowired
    private IBookMapper bookMapper;

    @Override
    public DataTableResult findList(BookQueryParam bookQueryParam) {
        Long count = bookMapper.findListCount(bookQueryParam);
        List<Book> bookList = bookMapper.findPageList(bookQueryParam);
        return new DataTableResult(bookQueryParam.getDraw(),count,count,bookList);
    }

    @Override
    public ServerResponse addBook(Book book) {
        bookMapper.insert(book);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBook(Long id) {
        bookMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArr = ids.split(",");
            List<Long> idList = new ArrayList<>();
            for (String s : idArr) {
                idList.add(Long.parseLong(s));
            }
            bookMapper.deleteBatchIds(idList);
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse findBook(Long id) {
        Book book = bookMapper.selectById(id);
        return ServerResponse.success(book);
    }

    @Override
    public ServerResponse updateBook(Book book) {
        bookMapper.updateById(book);
        return ServerResponse.success();
    }
}
