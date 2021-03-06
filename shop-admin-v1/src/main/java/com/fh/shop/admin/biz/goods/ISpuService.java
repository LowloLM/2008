package com.fh.shop.admin.biz.goods;

import com.fh.shop.admin.param.SpuParam;
import com.fh.shop.admin.param.SpuQueryParam;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ServerResponse;

public interface ISpuService {

    ServerResponse findSpuInfo(Long typeId);

    ServerResponse addSpu(SpuParam spuParam);

    DataTableResult findList(SpuQueryParam spuQueryParam);

    ServerResponse del(Long id,String rootPath);

    ServerResponse deleteBatch(String ids,String rootPath);

    ServerResponse findSpu(Long spuId);

    ServerResponse deleteSkuImage(Long key,String rootPath);

    ServerResponse updateSpu(SpuParam spuParam);

    ServerResponse updateStatus(Long id);

    ServerResponse updateStatus1(Long id);

    ServerResponse updateNewYes(Long id);

    ServerResponse updateNewNo(Long id);

    ServerResponse updateTuiYes(Long id);

    ServerResponse updateTuiNo(Long id);


}
