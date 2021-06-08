package com.fh.shop.api.book.controller;

import com.fh.shop.api.book.biz.IBookInfoService;
import com.fh.shop.api.book.param.BookParam;
import com.fh.shop.common.IResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/")
public class BookInfoController {

    @Resource(name = "responseService")
    private IBookInfoService responseService;


    @PostMapping("books/add")
    public IResponse add (@RequestBody BookParam bookParam){
        return responseService.add(bookParam);

    }
}
