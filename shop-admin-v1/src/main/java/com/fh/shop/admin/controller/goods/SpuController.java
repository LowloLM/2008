package com.fh.shop.admin.controller.goods;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.goods.ISpuService;
import com.fh.shop.admin.controller.BaseController;
import com.fh.shop.admin.param.SpuParam;
import com.fh.shop.admin.param.SpuQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SpuController extends BaseController {

    @Resource(name = "spuService")
    private ISpuService spuService;

    @RequestMapping(value = "/spu/toAdd",method = RequestMethod.GET)
    public String toAdd(){
        return "/goods/add";
    }

    @RequestMapping(value = "/spu/toList",method = RequestMethod.GET)
    public String toList(){
        return "/goods/list";
    }

    /**
     * 跳转修改页面
     * @return
     */
    @RequestMapping(value = "/spu/toEdit",method = RequestMethod.GET)
    public String toEdit(){
        return  "/goods/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/spu/findSpu",method = RequestMethod.GET)
    public ServerResponse findSpu(Long spuId){
        return spuService.findSpu(spuId);
    }

    @RequestMapping(value = "/spu/findSpuInfo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findSpuInfo(Long typeId){
        return spuService.findSpuInfo(typeId);
    }

    @RequestMapping(value = "/spu/addSpu",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "新增商品")
    public ServerResponse addSpu(SpuParam spuParam){
        return spuService.addSpu(spuParam);
    }

    @RequestMapping(value = "/spu/findList",method = RequestMethod.POST)
    @ResponseBody
    public DataTableResult findList(SpuQueryParam spuQueryParam){
        return spuService.findList(spuQueryParam);
    }

    @RequestMapping(value = "/spu/del",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "删除商品")
    public ServerResponse del(Long id,HttpServletRequest request){
        String rootPath = getRealPath("/", request);
        return spuService.del(id,rootPath);
    }

    @RequestMapping(value = "/spu/deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(info = "批量删除商品")
    public ServerResponse deleteBatch(String ids,HttpServletRequest request){
        String rootPath = getRealPath("/", request);
        return spuService.deleteBatch(ids,rootPath);
    }

    @RequestMapping(value = "/spu/deleteSkuImage",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse deleteSkuImage(Long key, HttpServletRequest request){
        String rootPath = getRealPath("/", request);
        return spuService.deleteSkuImage(key,rootPath);
    }

    @RequestMapping(value = "/spu/updateSpu",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateSpu(SpuParam spuParam){
        return spuService.updateSpu(spuParam);
    }

    @RequestMapping(value = "/spu/updateSJ",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateStatus(Long id){
        return spuService.updateStatus(id);
    }

    @RequestMapping(value = "/spu/updateXJ",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateStatus1(Long id){
        return spuService.updateStatus1(id);
    }

    @RequestMapping(value = "/spu/updateNewYes",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateNewYes(Long id){
        return spuService.updateNewYes(id);
    }

    @RequestMapping(value = "/spu/updateNewNo",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateNewNo(Long id){
        return spuService.updateNewNo(id);
    }

    @RequestMapping(value = "/spu/updateTuiYes",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateTuiYes(Long id){
        return spuService.updateTuiYes(id);
    }

    @RequestMapping(value = "/spu/updateTuiNo",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateTuiNo(Long id){
        return spuService.updateTuiNo(id);
    }


}
