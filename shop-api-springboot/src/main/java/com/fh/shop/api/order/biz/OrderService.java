package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.exception.ShopException;
import com.fh.shop.api.mapper.IOrderItemMapper;
import com.fh.shop.api.mapper.IOrderMapper;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.order.vo.OrderVo;
import com.fh.shop.api.param.OrderFindParam;
import com.fh.shop.api.param.OrderParam;
import com.fh.shop.api.po.Order;
import com.fh.shop.api.po.OrderItem;
import com.fh.shop.api.po.Sku;
import com.fh.shop.api.reclpient.mapper.IRecipientMapper;
import com.fh.shop.api.reclpient.po.Recipient;
import com.fh.shop.api.vo.CartItemVO;
import com.fh.shop.api.vo.CartVO;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service("orderService")
public class OrderService implements IOrderService {

    @Autowired
    private IOrderMapper OrderMapper;

    @Autowired
    private IOrderItemMapper orderItemMapper;

    @Autowired
    private IRecipientMapper recipientMapper;
    @Autowired
    private ISkuMapper skuMapper;

    @Override
    public ServerResponse addOrder(OrderParam orderParam, Long memberId) {
        Long recipientId = orderParam.getRecipientId();
        if (recipientId == null) {
            return ServerResponse.error(ResponseEnum.ORDER_RECIPENT_IS_NOT);
        }
        //根据会员ID从Redis中取出购物车数据
        String cartJson = RedisUtil.hget(KeyUtil.buildCartKey(memberId), Constans.CART_JSON_FIELD);
        CartVO cartVO = JSON.parseObject(cartJson, CartVO.class);
        //先减去库存【t_sku】
        //库存不足就不能下单
        List<CartItemVO> cartItemVoList = cartVO.getCartItemVoList();
        for (CartItemVO cartItemVO : cartItemVoList) {
            //查询库存
            Sku sku = skuMapper.selectById(cartItemVO.getSkuId());
            if (sku.getStock() < cartItemVO.getCount()) {
                //抛异常回滚
                throw new ShopException(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
            }
//            更新库存
            int count = skuMapper.updateStock(cartItemVO);
            if (count == 0) {
                throw new ShopException(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
            }
        }

        //插入订单表
        Order order = new Order();
        //雪花生成算法，唯一的id
        String orderId = IdWorker.getIdStr();
        order.setId(orderId);
        order.setCreateTime(new Date());
        order.setMemberId(memberId);
        order.setPayType(0);
        order.setStatus(Constans.ORDER_STATUS.WAIT_PYA);
        order.setTotalPrice(new BigDecimal(cartVO.getTotalPrice()));
        Recipient recipient = recipientMapper.selectById(recipientId);
        order.setRecipientAddr(recipient.getAddress());
        order.setRecipientName(recipient.getRecipientName());
        order.setRecipientPhone(recipient.getPhone());
        OrderMapper.insert(order);

        //订单明细
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        cartItemVoList.stream().forEach(x -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setMemberId(memberId);
            orderItem.setSkuCount(x.getCount());
            orderItem.setSkuId(x.getSkuId());
            orderItem.setSkuImage(x.getSkuImage());
            orderItem.setSkuName(x.getSkuName());
            orderItem.setSkuPrice(new BigDecimal(x.getPrice()));
            orderItem.setSubPrice(new BigDecimal(x.getSubPrice()));
            orderItems.add(orderItem);
        });
        if (orderItems.size() > 0) {
            //批量插入
            orderItemMapper.batchInster(orderItems);
            RedisUtil.delete(KeyUtil.buildCartKey(memberId));
            return ServerResponse.success();
        }
        RedisUtil.delete(KeyUtil.buildCartKey(memberId));
        return ServerResponse.success();
    }

    @Override
    public DataTableResult findOrder(OrderFindParam orderFindParam) {
        //获取总条数
        Long count = OrderMapper.findListCount(orderFindParam);
        //获取分页列表
        List<Order> ordersList = OrderMapper.findPageList(orderFindParam);
        List<OrderVo> collect = ordersList.stream().map(x -> {
            OrderVo orderVo = new OrderVo();
            orderVo.setId(x.getId());
            orderVo.setTotalPrice(x.getTotalPrice() + "");
            orderVo.setRecipientName(x.getRecipientName());
            orderVo.setRecipientPhone(x.getRecipientPhone());
            orderVo.setRecipientAddr(x.getRecipientAddr());
            orderVo.setCreateTime(x.getCreateTime());
            orderVo.setTotalCount(x.getTotalCount());
            orderVo.setStatus(x.getStatus());
            orderVo.setPayType(x.getPayType());
            Long memberId = orderFindParam.getMemberId();
            Order order = OrderMapper.selectById(memberId);
            orderVo.setMemberName(orderFindParam.getNickName());

            return orderVo;
        }).collect(Collectors.toList());
        return new DataTableResult(orderFindParam.getDraw(), count, count, collect);
    }

    @Override
    public ServerResponse cancelOrder(String id) {
        Order orderInfo = OrderMapper.selectById(id);
        if (orderInfo.getStatus() != Constans.ORDER_STATUS.WAIT_PYA) {
            return ServerResponse.error(ResponseEnum.ORDER_RECIPIENT_IS_NULL);
        }
        //辩证订单
        Order order = new Order();
        order.setId(id);
        order.setStatus(Constans.ORDER_STATUS.TRADE_CLOES);
        OrderMapper.updateById(order);
        //更新库存
        QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
        orderItemQueryWrapper.eq("orderId", id);
        List<OrderItem> orderItems = orderItemMapper.selectList(orderItemQueryWrapper);
        orderItems.stream().forEach(x -> {
            Long skuId = x.getSkuId();
            Long skuCount = x.getSkuCount();

            skuMapper.updateSkuStock(skuId, skuCount);
        });
        return ServerResponse.success();
    }
}
