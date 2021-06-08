package com.fh.shop.admin.mapper.log;

import com.fh.shop.admin.param.LogQueryParam;
import com.fh.shop.admin.po.log.Log;

import java.util.List;

public interface ILogMapper {

    /***
     * 自动添加日志数据
     * @param log
     */
    void addLog(Log log);

    /***
     * 查询总条数
     * @param logQueryParam
     * @return
     */
    Long findListCount(LogQueryParam logQueryParam);

    /***
     * 查询分页列表数据
     * @param logQueryParam
     * @return
     */
    List<Log> findPageList(LogQueryParam logQueryParam);

    /***
     * 导出Excel
     * 查询符合条件的数据集合
     * @param logQueryParam
     * @return
     */
    List<Log> findAllList(LogQueryParam logQueryParam);

}
