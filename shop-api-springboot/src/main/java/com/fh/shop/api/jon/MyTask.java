package com.fh.shop.api.jon;

import com.fh.shop.api.goods.biz.ISkuService;
import com.fh.shop.api.vo.SkuTaskVO;
import com.fh.shop.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyTask {

    @Autowired
    private ISkuService skuService;

    @Value("${stock.limit}")
    private int stockLimit;

    @Value("${stock.to}")
    private String to;

    @Scheduled(cron = " * * 5 * * ?")
    public void stockWarn(){
        List<SkuTaskVO> skuTaskVOS= skuService.skuTaskList(stockLimit);
        //生成表格
        String html = "<table >\n" +
                "  <tr>\n" +
                "\t<td>商品名</td>\n" +
                "\t<td>商品价格</td>\n" +
                "\t<td>库存</td>\n" +
                "\t<td>品牌</td>\n" +
                "\t<td>分类</td>\n" +
                "  </tr>\n" ;
        for (SkuTaskVO skuTaskVO : skuTaskVOS){
            html +=   "  <tr>\n" +
                    "\t<td>"+skuTaskVO.getSkuName()+"</td>\n" +
                    "\t<td>"+skuTaskVO.getPrice().toString()+"</td>\n" +
                    "\t<td>"+skuTaskVO.getStock()+"</td>\n" +
                    "\t<td>"+skuTaskVO.getBrandName()+"</td>\n" +
                    "\t<td>"+skuTaskVO.getCateName()+"</td>\n" +
                    "  </tr>\n" ;
        }
         html +=  "  </table>";

        MailUtil.sendMail("库存警告",html,to);
    }
}
