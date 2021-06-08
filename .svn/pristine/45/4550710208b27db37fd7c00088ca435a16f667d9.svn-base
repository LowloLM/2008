package com.fh.shop.admin.biz.log;

import com.fh.shop.admin.mapper.log.ILogMapper;
import com.fh.shop.admin.param.LogQueryParam;
import com.fh.shop.admin.po.log.Log;
import com.fh.shop.admin.vo.log.LogVo;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("logService")
public class ILogServiceImpl implements ILogService {

    @Autowired
    private ILogMapper logMapper;

    /***
     * 自动添加日志数据
     * @param log
     */
    @Override
    public void addLog(Log log) {
        logMapper.addLog(log);
    }

    /***
     * 查询分页列表数据
     * 条件查询
     * @param logQueryParam
     * @return
     */
    @Override
    public DataTableResult findList(LogQueryParam logQueryParam) {
        //获取总条数
        Long count = logMapper.findListCount(logQueryParam);
        //获取分页列表
        List<Log> logList = logMapper.findPageList(logQueryParam);
        //po转换vo
        List<LogVo> logVoList = new ArrayList<>();
        for (Log log : logList) {
            LogVo logVo = new LogVo();
            logVo.setId(log.getId());
            logVo.setUserName(log.getUserName());
            logVo.setRealName(log.getRealName());
            logVo.setInfo(log.getInfo());
            logVo.setInsertTime(DateUtil.date2str(log.getInsertTime(),DateUtil.FULL_YEAR));
            logVo.setParamInfo(log.getParamInfo());
            logVoList.add(logVo);
        }
        //组装数据
        return new DataTableResult(logQueryParam.getDraw(),count,count,logVoList);
    }

    /***
     * 导出Excel
     * 查询符合条件的数据集合
     * @param logQueryParam
     * @return
     */
    @Override
    public List<Log> findAllList(LogQueryParam logQueryParam) {
        List<Log> logList = logMapper.findAllList(logQueryParam);
        return logList;
    }

}
