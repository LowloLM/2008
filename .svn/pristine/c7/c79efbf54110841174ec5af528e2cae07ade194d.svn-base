package com.fh.shop.admin.controller.type;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.type.ITypeService;
import com.fh.shop.admin.param.TypeParam;
import com.fh.shop.admin.param.TypeQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TypeController {

    @Resource(name = "typeService")
    private ITypeService typeService;

    @RequestMapping(value = "/type/toAdd",method = RequestMethod.GET)
    public String toAdd(){
        return "/type/add";
    }

    @RequestMapping(value = "/type/toList",method = RequestMethod.GET)
    public String toList(){
        return "/type/list";
    }

    @RequestMapping(value = "/type/toEdit",method = RequestMethod.GET)
    public String toEdit(){
        return "/type/edit";
    }

    @RequestMapping(value = "/type/findInfo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findInfo(){
        return typeService.findInfo();
    }

    @RequestMapping(value = "/type/addType",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "新增类型")
    public ServerResponse addType(TypeParam typeParam){
        return typeService.addType(typeParam);
    }

    @RequestMapping(value = "/type/findList",method = RequestMethod.POST)
    @ResponseBody
    public DataTableResult findList(TypeQueryParam typeQueryParam){
        return typeService.findList(typeQueryParam);
    }

    @RequestMapping(value = "/type/findType",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findType(Long id){
        return typeService.findType(id);
    }

    @RequestMapping(value = "/type/updateType",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "修改类型")
    public ServerResponse updateType(TypeParam typeParam){
        return typeService.updateType(typeParam);
    }

    @RequestMapping(value = "/type/deleteType",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "删除类型")
    public ServerResponse deleteType(Long id){
        return typeService.deleteType(id);
    }

    @RequestMapping(value = "/type/deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "批量删除类型")
    public ServerResponse deleteBatch(String ids){
        return typeService.deleteBatch(ids);
    }

    @RequestMapping(value = "/type/findAllType",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findAllType(){
        return typeService.findAllType();
    }

}
