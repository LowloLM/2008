package com.fh.shop.admin.controller.cate;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.cate.ICateService;
import com.fh.shop.admin.param.CateParam;
import com.fh.shop.admin.po.cate.Cate;
import com.fh.shop.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class CateController {

    @Resource(name = "cateService")
    private ICateService cateService;

    @RequestMapping(value = "/cate/toList", method = RequestMethod.GET)
    public String toList() {
        return "/cate/list";
    }

    @RequestMapping(value = "/cate/findList",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findList(){
        return cateService.findAllList();
    }

    @RequestMapping(value = "/cate/addCate",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "新增分类")
    public ServerResponse addCate(Cate cate){
        return cateService.addCate(cate);
    }

    @RequestMapping(value = "/cate/deleteCate",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "删除分类")
    public ServerResponse deleteCate(String ids){
        return cateService.deleteCate(ids);
    }

    @RequestMapping(value = "/cate/findCate",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findCate(Long id){
        return cateService.findCate(id);
    }

    @RequestMapping(value = "/cate/updateCate",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "修改分类")
    public ServerResponse updateCate(CateParam cateParam){
        return cateService.updateCate(cateParam);
    }

    @RequestMapping(value = "/cate/addFatherCate",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "添加分类")
    public ServerResponse addFatherCate(Cate cate){
        return cateService.addFatherCate(cate);
    }

    @RequestMapping(value = "/cate/findCateChilds",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findCateChilds(Long id){
        return cateService.findCateChilds(id);
    }

}
