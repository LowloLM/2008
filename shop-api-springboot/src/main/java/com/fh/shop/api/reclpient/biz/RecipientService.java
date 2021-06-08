package com.fh.shop.api.reclpient.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.commone.Constans;
import com.fh.shop.api.reclpient.mapper.IRecipientMapper;
import com.fh.shop.api.reclpient.po.Recipient;
import com.fh.shop.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("recipientService")
public class RecipientService implements IRecipientService {

    @Autowired
    private IRecipientMapper recipientMapper;

    @Override
    public ServerResponse add(Recipient recipient) {
        recipientMapper.insert(recipient);
        return ServerResponse.success();
    }

    @Transactional(readOnly = true)
    @Override
    public ServerResponse findList(Long id) {
        QueryWrapper<Recipient> recipientQueryWrapper  = new QueryWrapper<>();
        recipientQueryWrapper.eq("memberId",id);
        List<Recipient> recipients = recipientMapper.selectList(recipientQueryWrapper);
        return ServerResponse.success(recipients);
    }

    @Override
    public ServerResponse updateStatus(Long id, Long memberId) {
        //先重置
        Recipient recipient = new Recipient();
        recipient.setStatus(Constans.NOT_DEFAULT);
        QueryWrapper<Recipient>recipientQueryWrapper  = new QueryWrapper<>();
        recipientQueryWrapper.eq("memberId",memberId);
        recipientMapper.update(recipient,recipientQueryWrapper);
        //再更新
        Recipient recipient1 = new Recipient();
        recipient1.setId(id);
        recipient1.setStatus(Constans.DEFAULT);
        recipientMapper.updateById(recipient1);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteRecipient(Long id) {
        recipientMapper.deleteById(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse selectById(Long id) {
        Recipient recipient = recipientMapper.selectById(id);
        return ServerResponse.success(recipient);
    }

    @Override
    public ServerResponse update(Recipient recipient) {
        recipientMapper.updateById(recipient);
        return ServerResponse.success();
    }
}
