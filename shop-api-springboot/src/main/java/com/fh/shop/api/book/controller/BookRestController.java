package com.fh.shop.api.book.controller;


import com.fh.shop.api.book.biz.IBookService;
import com.fh.shop.api.book.param.BookQueryParam;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/api")
@RestController

@Api(tags = "图书接口")
public class BookRestController {

    @Resource(name = "bookService")
    private IBookService bookService;

    @RequestMapping(value = "/books",method = RequestMethod.POST)
    @ApiOperation("新增图书")
    public ServerResponse addBook(Book book){
        return bookService.addBook(book);
    }

    @RequestMapping(value = "/books/{id}",method = RequestMethod.DELETE)
    @ApiOperation("删除图书")
    @ApiImplicitParam(name = "id",value = "图书id",dataType = "java.lang.Long",example = "0",required = true)
    public ServerResponse deleteBook(@PathVariable Long id){
        return bookService.deleteBook(id);
    }

    @RequestMapping(value = "/books",method = RequestMethod.DELETE)
    @ApiOperation("批量删除图书")
    @ApiImplicitParam(name = "ids",value = "图书id集合如：1,2,3",dataType = "java.lang.String",paramType = "query",required = true)
    public ServerResponse deleteBatch(String ids){
        return bookService.deleteBatch(ids);
    }

    @RequestMapping(value = "/books/{id}",method = RequestMethod.GET)
    @ApiOperation("根据id查询图书")
    @ApiImplicitParam(name = "id",value = "图书id",dataType = "java.long.Long",example = "0")
    public ServerResponse findBook(@PathVariable Long id){
        return bookService.findBook(id);
    }

    @RequestMapping(value = "/books",method = RequestMethod.PUT)
    @ApiOperation("修改图书")
    public ServerResponse updateBook(@RequestBody Book book){
        return bookService.updateBook(book);
    }

    @RequestMapping(value = "/books",method = RequestMethod.GET)
    @ApiOperation("查询图书")
    public DataTableResult findList(BookQueryParam bookQueryParam){
        return bookService.findList(bookQueryParam);
    }

}
