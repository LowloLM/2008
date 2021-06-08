package com.fh.shop.api.alipay.biz;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.config.AlipayConfig;
import com.fh.shop.api.mapper.IMemberMapper;
import com.fh.shop.api.mapper.IOrderItemMapper;
import com.fh.shop.api.mapper.IOrderMapper;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.mq.PayMessage;
import com.fh.shop.api.mq.PayProducer;
import com.fh.shop.api.po.Order;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class IAliPayServiceImpl implements IAliPayService {


    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IOrderItemMapper orderItemMapper;
    @Autowired
    private IMemberMapper memberMapper;
    @Autowired
    private ISkuMapper skuMapper;
    @Autowired
    private PayProducer payProducer;

    @Override
    public ServerResponse alipay(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null){
            return ServerResponse.error(ResponseEnum.PAY_ORDER_ID_IS_ERROR);
        }
        if (order.getPayType() != 0 ){
            return ServerResponse.error(ResponseEnum.PAY_ORDER_TYPE_IS_ERROR);
        }

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = order.getId();
        //付款金额，必填
        String total_amount = order.getTotalPrice().toString();
        //订单名称，必填
        String subject = order.getRecipientName();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        try {
            String  result = alipayClient.pageExecute(alipayRequest).getBody();
            System.out.println(result);
            return ServerResponse.success(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(String out_trade_no) {
        Order order = new Order();
        order.setId(out_trade_no);
        order.setStatus(Constans.ORDER_STATUS.PAY_SUCCESS);
        orderMapper.updateById(order);
    }

    @Override
    public String aliNotify(Map<String, String[]> requestParams) {

        try {
            Map<String, String> params = new HashMap<String, String>();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
                System.out.println(name+":"+valueStr);
                params.put(name, valueStr);
            }
            //调用SDK签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            if (signVerified){
                String trade_status =params.get("trade_status");
                if (trade_status.equals("TRADE_SUCCESS")){
                    //商家的业务处理
                    String out_trade_no = params.get("out_trade_no");
                    Order order = orderMapper.selectById(out_trade_no);
//              1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
                    if (order==null){
                        return "fail";
                    }
//                    2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
                    String orderPrice = order.getTotalPrice().toString();
                    String toal_amount = params.get("total_amount");
                    if (!toal_amount.equals(orderPrice)){
                        return "fail";
                    }
//                    3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
                    String seller_id = params.get("seller_id");
                    if (!seller_id.equals(Constans.SELLER_URID)){
                        return "fail";
                    }
//                    4、验证app_id是否为该商户本身。
                    String app_id = params.get("app_id");
                    if (!app_id.equals(AlipayConfig.app_id)){
                        return "fail";
                    }
                    //之前已经支付成功就不需要后续的业务处理
                    if (order.getStatus() != Constans.ORDER_STATUS.WAIT_PYA){
                        return "success";
                    }
                    //更改订单状态【主要业务】
                    Long memberId = order.getMemberId();
                    String total_amount = params.get("total_amount");
                    Order order1 = new Order();
                    order1.setId(out_trade_no);
                    order1.setStatus(Constans.ORDER_STATUS.PAY_SUCCESS);
                    orderMapper.updateById(order1);
                    //加积分
//                    Long memberId = order.getMemberId();
//                    double totalPrice = Math.floor(Double.parseDouble(total_amount));
//                    memberMapper.updateScore(memberId,totalPrice);
                    //加销量
////                    QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
////                    orderItemQueryWrapper.eq("orderId",out_trade_no);
////                    List<OrderItem> orderItemList = orderItemMapper.selectList(orderItemQueryWrapper);
////                    orderItemList.stream().forEach(x ->{
////                        skuMapper.updateSalesVolume(x.getSkuId(),x.getSkuCount());
////                    });
                    PayMessage payMessage = new PayMessage();
                    payMessage.setMemberId(order.getMemberId());
                    payMessage.setTotalAmount(total_amount);
                    payMessage.setOrderId(out_trade_no);
                    payProducer.sendMessage(payMessage);
                    return "success";
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "fail";
    }
}
