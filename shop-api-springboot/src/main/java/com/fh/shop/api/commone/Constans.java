package com.fh.shop.api.commone;

public class Constans {

    public static final String SECRET ="alkh209h21jkbcx893nf31";

    public static final int TOKEN_EXPIRE= 30 * 60;

    public static final String CURR_MEMBER= "member";

    public static final String CURR_CLIENT= "Client";

    public static final  int REQUEST_ERROR = 1;

    public static final  String  ACTIVE = "1";

    public static final int OK = 1;


    public static final int STATUS = 0;

    public static final String DEFAULT = "1";
    public static final String  NOT_DEFAULT= "0";

    public static final String CART_JSON_FIELD="cartJson";

    public static final String CART_COUNT_FIELD="cartCount";

    public static final String SELLER_URID="2088621955811475";
    public interface ORDER_STATUS{
        int WAIT_PYA=0;
        int PAY_SUCCESS=10;
        int TRADE_CLOES=40;
    }
}
