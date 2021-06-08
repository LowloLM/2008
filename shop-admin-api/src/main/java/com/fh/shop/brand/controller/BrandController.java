package com.fh.shop.brand.controller;

import com.fh.shop.brand.biz.IBrandService;
import com.fh.shop.brand.param.BrandQueryParam;
import com.fh.shop.brand.po.Brand;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@Controller
@CrossOrigin
@RequestMapping("/brands")
public class BrandController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    @Resource(name = "brandService")
    private IBrandService brandService;


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
    @RequestMapping(value = "/addBrandInfo" ,method = RequestMethod.POST)
    public ServerResponse addBrandInfo(@RequestBody Brand brand){
        return  brandService.addBrand(brand);

    }


    /***
     * 查询
     * @param brandQueryParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findList" ,method = RequestMethod.POST)
    public ServerResponse findList(BrandQueryParam brandQueryParam){
        return brandService.findList(brandQueryParam);
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/brand/delete")
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
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/brand/deleteBatch" ,method = RequestMethod.POST)
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
    @GetMapping("/{id}")
    public ServerResponse findBrand(@PathVariable Long id){
        return brandService.findBrand(id);
    }

    /***
     * 修改
     * @param brand
     * @return
     */
    @ResponseBody
    @PutMapping
    public ServerResponse updateBrand(@RequestBody Brand brand, HttpServletRequest request){
        String rootRealPath = getRealPath("/",request);
        return brandService.updateBrand(brand,rootRealPath);
    }


    @ResponseBody
    @RequestMapping(value = "/brand/findBrandList")
    public ServerResponse findBrandList(Long cateId){
        return brandService.findBrandList(cateId);
    }


    @RequestMapping(value = "brand/uploadImageOss",method = RequestMethod.POST)
    public @ResponseBody ServerResponse uploadImageOss(MultipartFile file, HttpServletRequest request){
        try {
            //IO流读取文件
            InputStream inputStream = file.getInputStream();
            //获取文件原名
            String originalFilename = file.getOriginalFilename();

            String uploadFileName = OSSUtil.upload(originalFilename, inputStream);

            return ServerResponse.success(uploadFileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
