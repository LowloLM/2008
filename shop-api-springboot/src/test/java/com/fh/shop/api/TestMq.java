package com.fh.shop.api;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.mq.TestProducer;
import com.fh.shop.api.testMq.MsgInfo;
import com.fh.shop.api.testMq.ProudcerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMq {

//       @Autowired
//    private TestProducer testProducer;
    @Autowired
    private ProudcerTest proudcerTest;

//    @Test
//    public void test(){
//        testProducer.send("+++++++++++fh+++++++++++");
//    }

//    @Test
//    public void test1(  ){
//        testProducer.sendTopic("++aaaaaaaaa+++fh+xxxxxxxxxxxxxx+","test");
//    }

    @Test
    public void test1(){
        for (int i = 1; i <10 ; i++) {
            MsgInfo msgInfo = new MsgInfo();
            msgInfo.setMessage("XXXXXX"+i);
            String s = UUID.randomUUID().toString();
            msgInfo.setMsgId(s);
            String s1 = JSON.toJSONString(msgInfo);
            proudcerTest.sendMsg(s1);
        }
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
