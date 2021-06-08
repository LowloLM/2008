package com.fh.shop.api.reclpient.biz;

import com.fh.shop.api.reclpient.po.Recipient;
import com.fh.shop.common.ServerResponse;

public interface IRecipientService {

    public ServerResponse add(Recipient recipient);

    ServerResponse findList(Long id);

    ServerResponse updateStatus(Long id, Long memberId);

    ServerResponse deleteRecipient(Long id);

    ServerResponse selectById(Long id);

    ServerResponse update(Recipient recipient);
}
