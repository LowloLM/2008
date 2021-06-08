package com.fh.shop.cate.controller;

import com.fh.shop.cate.biz.ICateService;
import com.fh.shop.cate.param.CateParam;
import com.fh.shop.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "分类接口")
public class CateController {

    @Resource(name = "cateService")
    private ICateService cateService;

    @GetMapping(value = "/cates")
    @ApiOperation("查询分类")
    @ResponseBody
    public ServerResponse queryList(){
        return cateService.queryList();
    }

    @RequestMapping(value = "/cate/findList",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findList(){
        return cateService.findAllList();
    }

    @RequestMapping(value = "/cate/addCate",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增分类")
    public ServerResponse addCate(Cate cate){
        return cateService.addCate(cate);
    }

    @RequestMapping(value = "/cate/deleteCate",method = RequestMethod.POST)
    @ApiOperation("根绝Id删除分类")
    @ResponseBody
    public ServerResponse deleteCate(String ids){
        return cateService.deleteCate(ids);
    }

    @RequestMapping(value = "/cate/updateCate",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("修改分类")
    public ServerResponse updateCate(CateParam cateParam){
        return cateService.updateCate(cateParam);
    }

    @RequestMapping(value = "/cate/addFatherCate",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增根节点")
    public ServerResponse addFatherCate(Cate cate){
        return cateService.addFatherCate(cate);
    }

    @RequestMapping(value = "/cate/findCateChilds",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询子节点")
    public ServerResponse findCateChilds(Long id){
        return cateService.findCateChilds(id);
    }

}
