package com.fh.shop.api.commone;

public class KeyUtil {

    public static String buildMemberKey(Long id){
        return "member:"+id;
    }

    public static String buildNoceKey(String nonce){
        return "nonce:"+nonce;
    }

    public static String buildImageCodeKey(String id){
        return "image:code:"+id;
    }

    public static String activateMailUrl(String uuid){
        return "<a href='http://localhost:8083/api/member/activate?id="+uuid+"'>点击激活</a>";
    }
    public static String buildActiveMemberKey(String uuid){
        return "active:member:"+uuid;
    }

    public static String buildCartKey(Long memberId){
        return "cart:"+memberId;
    }

    public static String buildTokenKey (String token){
        return "token:"+token;
    }
}
