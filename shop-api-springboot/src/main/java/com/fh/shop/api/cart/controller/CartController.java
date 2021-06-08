package com.fh.shop.api.cart.controller;

import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.cart.biz.CartService;
import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/carts")
@RestController
@Api(tags = "购物车接口")
public class CartController {

    @Resource(name = "cartService")
    private ICartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Check
    @PostMapping("/addCartItem")
    @ApiOperation("添加信息到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId",value = "商品ID",dataType = "java.lang.Long",required = true),
            @ApiImplicitParam(name = "count",value = "商品数量",dataType = "java.lang.Long",required = true),
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true ,paramType = "header"),
    })
    public ServerResponse addCartItem(Long skuId ,Long count){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId=memberVO.getId();
        return cartService.addCartItem(memberId,skuId,count);
    }

    @Check
    @GetMapping("/findCart")
    @ApiOperation("查找购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true ,paramType = "header"),
    })
    public ServerResponse findCart(){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVO.getId();
        return cartService.findCart(memberId);
    }


    @Check
    @GetMapping("/findCartCount")
    @ApiOperation("查找购物车商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true ,paramType = "header"),
    })
    public ServerResponse findCartCount(){
        MemberVO memberVo =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVo.getId();
        return cartService.findCartCount(memberId);
    }

    @Check
    @DeleteMapping("/deleteCartItem")
    @ApiOperation("删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true ,paramType = "header"),
    })
    public ServerResponse deleteCartItem(Long skuId){
        MemberVO memberVo =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVo.getId();
        return cartService. deleteCartItem( skuId,memberId);
    }

    @Check
    @DeleteMapping("/deleteBatch")
    @ApiOperation("批量删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true ,paramType = "header"),
    })
    public ServerResponse deleteBatch(String ids){
        MemberVO memberVo =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVo.getId();
        return cartService. deleteBatch(memberId,ids);
    }

}
