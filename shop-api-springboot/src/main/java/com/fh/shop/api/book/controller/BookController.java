package com.fh.shop.api.book.controller;


import com.fh.shop.api.book.biz.IBookService;
import com.fh.shop.api.book.param.BookQueryParam;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.common.BaseController;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@RequestMapping("/api")
@RestController
@CrossOrigin
public class BookController extends BaseController {

    @Resource(name = "bookService")
    private IBookService bookService;

    @RequestMapping(value = "/book/findList" ,method = RequestMethod.POST)
    public DataTableResult findList(BookQueryParam bookQueryParam){
        return bookService.findList(bookQueryParam);
    }

    @RequestMapping(value = "/book/addBook" ,method = RequestMethod.POST)
    public ServerResponse addBook(Book book){
        return bookService.addBook(book);
    }

    @RequestMapping(value = "/book/deleteBook" ,method = RequestMethod.POST)
    public ServerResponse deleteBook(Long id){
        return bookService.deleteBook(id);
    }

    @RequestMapping(value = "/book/deleteBatch" ,method = RequestMethod.POST)
    public ServerResponse deleteBatch(String ids){
        return bookService.deleteBatch(ids);
    }

    @RequestMapping(value = "/book/findBook" ,method = RequestMethod.GET)
    public ServerResponse findBook(Long id){
        return bookService.findBook(id);
    }

    @RequestMapping(value = "/book/updateBook" ,method = RequestMethod.POST)
    public ServerResponse updateBook(Book book){
        return bookService.updateBook(book);
    }

}
