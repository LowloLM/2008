package com.fh.shop.admin.biz.log;

import com.fh.shop.admin.param.LogQueryParam;
import com.fh.shop.admin.po.log.Log;
import com.fh.shop.common.DataTableResult;

import java.util.List;

public interface ILogService {

    /***
     * 自动添加日志数据
     * @param log
     */
    void addLog(Log log);

    /***
     * 查询分页列表数据
     * @param logQueryParam
     * @return
     */
    DataTableResult findList(LogQueryParam logQueryParam);

    /***
     * 导出Excel
     * 查询符合条件的数据集合
     * @param logQueryParam
     * @return
     */
    List<Log> findAllList(LogQueryParam logQueryParam);

}
