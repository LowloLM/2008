package com.fh.shop.admin.controller.brand;

import com.fh.shop.admin.annotation.LogInfo;
import com.fh.shop.admin.biz.brand.IBrandService;
import com.fh.shop.admin.controller.BaseController;
import com.fh.shop.admin.param.BrandQueryParam;
import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class BrandController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    @Resource(name = "brandService")
    private IBrandService brandService;

    /***
     * 跳转到新增页面
     * @return
     */
    @RequestMapping(value = "/brand/toAdd", method = RequestMethod.GET)
    public String toAdd(){
        return "/brand/add";
    }

    /***
     * 新增
     * @param brand
     * @return
     */
    @RequestMapping(value = "/brand/addBrand" ,method = RequestMethod.POST)
    public String addBrand(Brand brand){
        brandService.addBrand(brand);
        LOGGER.info("brand222");
        return "redirect:/brand/toList.jhtml";
    }

    /***
     * 新增(弹框)
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/addBrandInfo" ,method = RequestMethod.POST)
    @LogInfo(info = "新增品牌")
    public ServerResponse addBrandInfo(Brand brand){
        brandService.addBrand(brand);
        return ServerResponse.success();
    }

    /***
     * 跳转到列表展示页面
     * @return
     */
    @RequestMapping(value = "/brand/toList" ,method = RequestMethod.GET)
    public String toList(){
        return "/brand/list";
    }

    /***
     * 查询
     * @param brandQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/findList" ,method = RequestMethod.POST)
    public DataTableResult findList(BrandQueryParam brandQueryParam){
        return brandService.findList(brandQueryParam);
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/del" ,method = RequestMethod.POST)
    @LogInfo(info = "删除品牌")
    public ServerResponse del(Long id,HttpServletRequest request){
        String realPath = getRealPath("/",request);
        brandService.del(id,realPath);
        return ServerResponse.success();
    }

    /***
     * 批量删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/deleteBatch" ,method = RequestMethod.POST)
    @LogInfo(info = "批量删除品牌")
    public ServerResponse deleteBatch(String ids,HttpServletRequest request){
        String realPath = getRealPath("/",request);
        return brandService.deleteBatch(ids,realPath);
    }

    /***
     * 回显
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/findBrand" ,method = RequestMethod.GET)
    public ServerResponse findBrand(Long id){
        return brandService.findBrand(id);
    }

    /***
     * 修改
     * @param brand
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brand/update" ,method = RequestMethod.POST)
    @LogInfo(info = "更新品牌")
    public ServerResponse updateBrand(Brand brand, HttpServletRequest request){
        String rootRealPath = getRealPath("/",request);
        return brandService.updateBrand(brand,rootRealPath);
    }

    @ResponseBody
    @RequestMapping(value = "/brand/findBrandList")
    public ServerResponse findBrandList(Long cateId){
        return brandService.findBrandList(cateId);
    }

}
