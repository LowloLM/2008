package com.fh.shop.api.order.controller;

import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.annotation.Token;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.api.order.biz.IOrderService;

import com.fh.shop.api.param.OrderFindParam;
import com.fh.shop.api.param.OrderParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Resource(name ="orderService" )
    private IOrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation("新增订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payType", value = "订单状态", dataType = "java.lang.Integer", required = true),
            @ApiImplicitParam(name = "recipientId", value = "收件人id", dataType = "java.lang.Long", required = true),
            @ApiImplicitParam(name = "x-auth",value = "value",dataType = "java.lang.String",required = true,paramType = "header")
    })
    @PostMapping("/addOrder")
    @Check
    @Token
    public ServerResponse addOrder(OrderParam orderParam){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVO.getId();
        return orderService.addOrder( orderParam,memberId);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "value",dataType = "java.lang.String",required = true,paramType = "header")
    })
    @ApiOperation("查询订单")
    @Check
    @PostMapping("/findOrder")
    public DataTableResult findOrder(OrderFindParam orderFindParam){
        MemberVO memberVO=(MemberVO)request.getAttribute(Constans.CURR_MEMBER);
        orderFindParam.setMemberId(memberVO.getId());
        orderFindParam.setNickName(memberVO.getNickName());
        return orderService.findOrder(orderFindParam);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "value",dataType = "java.lang.String",required = true,paramType = "header")
    })
    @ApiOperation("取消订单")
    @Check
    @PostMapping("/cancelOrder/{id}")
    public ServerResponse cancelOrder(String id){
        return orderService.cancelOrder(id);
    }
}
