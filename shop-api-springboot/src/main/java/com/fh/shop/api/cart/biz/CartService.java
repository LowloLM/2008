package com.fh.shop.api.cart.biz;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.commone.KeyUtil;
import com.fh.shop.api.mapper.ISkuMapper;
import com.fh.shop.api.po.Sku;
import com.fh.shop.api.vo.CartItemVO;
import com.fh.shop.api.vo.CartVO;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.BigDecimalUtil;
import com.fh.shop.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("cartService")
@Transactional(rollbackFor = Exception.class)
public class CartService implements ICartService {

    @Autowired
    private ISkuMapper skuMapper;

    @Override
    public ServerResponse addCartItem(Long memberId, Long skuId, Long count) {
        if (count > 10){
            return ServerResponse.error(ResponseEnum.CART_SKU_COUNT_LIMIT);
        }
        Sku sku = skuMapper.selectById(skuId);

        //商品是否存在
        if(sku == null){
            return ServerResponse.error(ResponseEnum.CART_SKU_IS_NULL);
        }
        //商品是否上架
        if(sku.getStatus() == Constans.STATUS){
            return ServerResponse.error(ResponseEnum.CART_SKU_IS_DOWN);
        }
        //商品的库存量大于等于购买的数量
        Integer stock = sku.getStock();
        if(stock.intValue() < count){
            return ServerResponse.error(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
        }
        //会员是否有购物车
        String key = KeyUtil.buildCartKey(memberId);
        boolean exist = RedisUtil.exist(key);

        //如果没有购物车
        if(!exist){
            if (count<0){
                return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
            }
            //创建一个购物车，直接把商品加入到购物车
            CartVO cartVo = new CartVO();
            CartItemVO cartItemVo = new CartItemVO();
            cartItemVo.setCount(count);
            String price = sku.getPrice().toString();
            cartItemVo.setPrice(price);
            cartItemVo.setSkuId(sku.getId());
            cartItemVo.setSkuImage(sku.getImage());
            cartItemVo.setSkuName(sku.getSkuName());
            BigDecimal subPrice = BigDecimalUtil.mul(price, count+"");
            cartItemVo.setSubPrice(subPrice.toString());
            cartVo.getCartItemVoList().add(cartItemVo);
            cartVo.setTotalCount(count);
            cartVo.setTotalPrice(cartItemVo.getSubPrice());
            String cartVoStr = JSON.toJSONString(cartVo);
            //更新购物车【redis中的购物车】
          //  RedisUtil.set(key, JSON.toJSONString(cartVo));
            RedisUtil.hset(key,Constans.CART_JSON_FIELD,cartVoStr);
            RedisUtil.hset(key,Constans.CART_COUNT_FIELD,cartVo.getTotalCount()+"");

        } else {
            //如果有购物车
            String cartJson = RedisUtil.hget(key, Constans.CART_JSON_FIELD);
            CartVO cartVo = JSON.parseObject(cartJson, CartVO.class);
            List<CartItemVO> cartItemVOList=cartVo.getCartItemVoList();
            Optional<CartItemVO> item = cartItemVOList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
            if (item.isPresent()){
                CartItemVO cartItemVO = item.get();
                //限购10个
                long itemCount = cartItemVO.getCount() + count;
                if (itemCount > 10){
                    return ServerResponse.error(ResponseEnum.CART_SKU_COUNT_LIMIT);
                }
                if (itemCount <= 0){
                    //删除商品
                    cartItemVOList.removeIf(x -> x.getSkuId().longValue() == cartItemVO.getSkuId().longValue());
                    if (cartItemVOList.size() == 0){
                        RedisUtil.delete(key);
                        return ServerResponse.success();
                    }
                    //更新
                    updateCart(key,cartVo);
                    return ServerResponse.success();
                }
                cartItemVO.setCount(cartItemVO.getCount()+count);
                BigDecimal subPrice = new BigDecimal(cartItemVO.getSubPrice());
                String subPriceStr = subPrice.add(BigDecimalUtil.mul(cartItemVO.getPrice(), count + "")).toString();
                cartItemVO.setSubPrice(subPriceStr);
                //更新购物车
                updateCart(key, cartVo);
            } else {
            //购物车有这款商品，找到这款商品，更新商品的数量，小计
                CartItemVO cartItemVo = new CartItemVO();
                cartItemVo.setCount(count);
                String price = sku.getPrice().toString();
                cartItemVo.setPrice(price);
                cartItemVo.setSkuId(sku.getId());
                cartItemVo.setSkuImage(sku.getImage());
                cartItemVo.setSkuName(sku.getSkuName());
                BigDecimal subPrice = BigDecimalUtil.mul(price, count.toString());
                cartItemVo.setSubPrice(subPrice.toString());

                List<CartItemVO> cartItemVOS = cartVo.getCartItemVoList();

                cartItemVOS.add(cartItemVo);

                long totalCount = 0 ;
                BigDecimal totalPrice = new BigDecimal(0);
                for(CartItemVO itemVO:cartItemVOS){
                    totalCount += itemVO.getCount();
                    totalPrice= totalPrice.add(new BigDecimal(cartItemVo.getSubPrice()));
                }
                cartVo.setCartItemVoList(cartItemVOS);
                cartVo.setTotalCount(totalCount);
                cartVo.setTotalPrice(totalPrice.toString());
                //更新购物车【redis中的购物车】
              //  RedisUtil.set(key, JSON.toJSONString(cartVo));
                RedisUtil.hset(key,Constans.CART_JSON_FIELD,JSON.toJSONString(cartVo));
                RedisUtil.hset(key,Constans.CART_COUNT_FIELD,cartVo.getTotalCount()+"");
            }
        }
        return ServerResponse.success();
    }

    private void updateCart(String key,  CartVO cartVo) {
        //更新购物车
        List<CartItemVO> cartItemVOS = cartVo.getCartItemVoList();
        long totalCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemVO itemVO : cartItemVOS) {
            totalCount += itemVO.getCount();
            totalPrice = totalPrice.add(new BigDecimal(itemVO.getSubPrice()));
        }
        cartVo.setCartItemVoList(cartItemVOS);
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        //更新购物车【redis中的购物车】
      // RedisUtil.set(key, JSON.toJSONString(cartVo));
        RedisUtil.hset(key,Constans.CART_JSON_FIELD,JSON.toJSONString(cartVo));
        RedisUtil.hset(key,Constans.CART_COUNT_FIELD,cartVo.getTotalCount()+"");

    }

    @Override
    public ServerResponse findCart(Long memberId) {
        String cartJson = RedisUtil.hget(KeyUtil.buildCartKey(memberId),Constans.CART_JSON_FIELD);
        CartVO cartVO = JSON.parseObject(cartJson, CartVO.class);
        return ServerResponse.success(cartVO);
    }

    @Override
    public ServerResponse findCartCount(Long memberId) {
        String count = RedisUtil.hget(KeyUtil.buildCartKey(memberId),Constans.CART_COUNT_FIELD);
        return ServerResponse.success(count);
    }

    @Override
    public ServerResponse deleteCartItem(Long skuId, Long memberId) {
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key,Constans.CART_COUNT_FIELD);
        CartVO cartVO = JSON.parseObject(cartJson, CartVO.class);
        List<CartItemVO> cartItemVoList = cartVO.getCartItemVoList();
        Optional<CartItemVO> itemVo = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
        if (!itemVo.isPresent()){
            return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
        }
        cartItemVoList.removeIf(x -> x.getSkuId().longValue() == skuId.longValue());
        if (cartItemVoList.size() == 0){
            RedisUtil.delete(key);
            return ServerResponse.success();
        }
        //更新购物车
        updateCart(key,cartVO);
        return ServerResponse.success();
    }



    @Override
    public ServerResponse deleteBatch(Long memberId, String ids) {
        if (StringUtils.isEmpty(ids)){
            return ServerResponse.error(ResponseEnum.CART_BATCH_DELETE_NO_SELECT);
        }
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key,Constans.CART_COUNT_FIELD);
        CartVO cartVO = JSON.parseObject(cartJson, CartVO.class);
        List<CartItemVO> cartItemVoList = cartVO.getCartItemVoList();
        Arrays.stream(ids.split(",")).forEach(x -> cartItemVoList.removeIf(y -> y.getSkuId().longValue() == Long.parseLong(x)));
        if (cartItemVoList.size() == 0){
            return ServerResponse.success();
        }
        //更新购物车
        updateCart(key,cartVO);
        return ServerResponse.success();
    }

}
