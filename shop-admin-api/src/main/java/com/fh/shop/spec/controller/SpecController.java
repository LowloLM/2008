package com.fh.shop.spec.controller;

import com.fh.shop.brand.controller.BaseController;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.spec.biz.ISpecService;
import com.fh.shop.spec.param.SpecAddParam;
import com.fh.shop.spec.param.SpecParam;
import com.fh.shop.spec.param.SpecQueryParam;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "规格接口")
public class SpecController extends BaseController {

    @Resource(name = "specService")
    private ISpecService specService;

    /***
     * 新增
     * @param specParam
     * @return
     */
   /* @RequestMapping(value = "/spec/add",method = RequestMethod.POST)
    public ServerResponse add(SpecParam specParam){
        return specService.addSpec(specParam);
    }*/

    @RequestMapping(value = "/spec/addSpec",method = RequestMethod.POST)
    public ServerResponse addSpec(@RequestBody List<SpecAddParam> specParam){
        return specService.addSpec(specParam);
    }


    /***
     * 查询分页列表
     * @param
     * @return
     */
    @RequestMapping(value = "/spec/findList",method = RequestMethod.POST)
    public ServerResponse findList( SpecQueryParam specQueryParam){
        return specService.findList(specQueryParam);
    }


   @GetMapping("/spec/findSpec")
    public ServerResponse findSpec(Long id){
        return specService.findSpec(id);
    }

    @RequestMapping(value = "/spec/updateSpec",method = RequestMethod.POST)
    public ServerResponse updateSpec(@RequestBody  SpecAddParam specAddParam){
        return specService.updateSpec(specAddParam);
    }

    @RequestMapping(value = "/spec/deleteSpec",method = RequestMethod.POST)
    public ServerResponse deleteSpec(Long id){
        return specService.deleteSpec(id);
    }

    @RequestMapping(value = "/spec/deleteBatch",method = RequestMethod.POST)
    public ServerResponse deleteBatch(String ids){
        return specService.deleteBatch(ids);
    }

}
