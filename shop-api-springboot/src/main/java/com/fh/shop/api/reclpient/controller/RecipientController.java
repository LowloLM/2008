package com.fh.shop.api.reclpient.controller;

import com.fh.shop.api.annotation.Check;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.member.vo.MemberVO;
import com.fh.shop.api.reclpient.biz.IRecipientService;
import com.fh.shop.api.reclpient.po.Recipient;
import com.fh.shop.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/recipients")
public class RecipientController {

    @Resource(name ="recipientService" )
    private IRecipientService recipientService;

    @Autowired
    private HttpServletRequest request;


    @Check
    @PostMapping("/add")
    public ServerResponse add(Recipient recipient){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long id = memberVO.getId();
        recipient.setMemberId(id);
        return recipientService.add(recipient);
    }

    @GetMapping("/findList")
    @Check
    public ServerResponse fiindList(){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long id = memberVO.getId();
        return recipientService.findList(id);
    }
    @Check
    @PostMapping("/updateStatus")
    public ServerResponse updateStatus(Long id){
        MemberVO memberVO =(MemberVO) request.getAttribute(Constans.CURR_MEMBER);
        Long memberId = memberVO.getId();
        return recipientService.updateStatus( id,memberId);
    }

    @Check
    @PostMapping("/deleteRecipient")
    public ServerResponse deleteRecipient(Long id){
        return recipientService.deleteRecipient(id);
    }

    @Check
    @PostMapping("/update")
    public ServerResponse update(Recipient recipient){
        return recipientService.update(recipient);
    }

    @Check
    @GetMapping("/selectById")
    public ServerResponse selectById(Long id){
        return recipientService.selectById(id);
    }




}
