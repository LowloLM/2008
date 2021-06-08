package com.fh.shop.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartVO implements Serializable {

   private List<CartItemVO> cartItemVoList = new ArrayList<>();

   private Long totalCount;

   private String totalPrice;


}
