package com.fh.shop.type.controller;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.type.biz.ITypeService;
import com.fh.shop.type.param.TypeParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TypeController {

    @Resource(name = "typeService")
    private ITypeService typeService;

    @RequestMapping(value = "/type/findInfo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findInfo(){
        return typeService.findInfo();
    }

    @PostMapping("/addType")
    public ServerResponse addType(@RequestBody TypeParam typeParam){
        return typeService.addType(typeParam);
    }

    @GetMapping("/findType")
    public ServerResponse findType(Long id){
        return typeService.findType(id);
    }
}
