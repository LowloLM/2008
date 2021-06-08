package com.fh.shop.admin.controller.spec;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.spec.ISpecService;
import com.fh.shop.admin.controller.BaseController;
import com.fh.shop.admin.param.SpecParam;
import com.fh.shop.admin.param.SpecQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class SpecController extends BaseController {

    @Resource(name = "specService")
    private ISpecService specService;

    /***
     * 跳转到新增页面
     * @return
     */
    @RequestMapping(value = "/spec/toAdd",method = RequestMethod.GET)
    public String toAdd(){
        return "/spec/add";
    }

    /***
     * 新增
     * @param specParam
     * @return
     */
    @RequestMapping(value = "/spec/add",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "新增规格")
    public ServerResponse add(SpecParam specParam){
        return specService.addSpec(specParam);
    }

    /***
     * 跳转到分页列表页面
     * @return
     */
    @RequestMapping(value = "/spec/toList",method = RequestMethod.GET)
    public String toList(){
        return "/spec/list";
    }

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    @RequestMapping(value = "/spec/findList",method = RequestMethod.POST)
    @ResponseBody
    public DataTableResult findList(SpecQueryParam specQueryParam){
        return specService.findList(specQueryParam);
    }

    /***
     * 跳转到修改页面
     * @return
     */
    @RequestMapping(value = "/spec/toEdit",method = RequestMethod.GET)
    public String toEdit(){
        return "/spec/edit";
    }

    @RequestMapping(value = "/spec/findSpec",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse findSpec(Long id){
        return specService.findSpec(id);
    }

    @RequestMapping(value = "/spec/updateSpec",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "修改规格")
    public ServerResponse updateSpec(SpecParam specParam){
        return specService.updateSpec(specParam);
    }

    @RequestMapping(value = "/spec/deleteSpec",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "删除规格")
    public ServerResponse deleteSpec(Long id){
        return specService.deleteSpec(id);
    }

    @RequestMapping(value = "/spec/deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "批量删除规格")
    public ServerResponse deleteBatch(String ids){
        return specService.deleteBatch(ids);
    }

}
