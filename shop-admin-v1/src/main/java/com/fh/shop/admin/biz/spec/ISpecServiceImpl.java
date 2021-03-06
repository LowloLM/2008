package com.fh.shop.admin.biz.spec;

import com.fh.shop.admin.mapper.spec.ISpecMapper;
import com.fh.shop.admin.mapper.spec.ISpecValueMapper;
import com.fh.shop.admin.param.SpecParam;
import com.fh.shop.admin.param.SpecQueryParam;
import com.fh.shop.admin.po.spec.Spec;
import com.fh.shop.admin.po.spec.SpecValue;
import com.fh.shop.admin.vo.spec.SpecVo;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("specService")
public class ISpecServiceImpl implements ISpecService {

    @Autowired
    private ISpecMapper specMapper;

    @Autowired
    private ISpecValueMapper specValueMapper;

    /***
     * 新增多条数据
     * @param specParam
     * @return
     */
    @Override
    public ServerResponse addSpec(SpecParam specParam) {
        //获取到参数信息
        String specNames = specParam.getSpecNames();//颜色，内存
        String specNameSorts = specParam.getSpecNameSorts();//1,2
        //黑色=1，红色=2；8G=1，16G=2，32G=3
        String specValueInfos = specParam.getSpecValueInfos();
        //进行非空验证
        if(StringUtils.isEmpty(specNames) || StringUtils.isEmpty(specNameSorts)
                                          || StringUtils.isEmpty(specValueInfos)){
            return ServerResponse.error(ResponseEnum.SPEC_INFO_IS_NULL);
        }
        //对参数信息进行拆分
        String[] specNameArr = specNames.split(",");
        String[] specNameSortArr = specNameSorts.split(",");
        String[] specValueInfoArr = specValueInfos.split(";");
        for (int i = 0; i <specNameArr.length ; i++) {
            String specName = specNameArr[i];
            String specNameSort = specNameSortArr[i];
            //创建规格对象
            Spec spec = new Spec();
            //给对象属性赋值
            spec.setSpecName(specName);
            spec.setSort(Integer.parseInt(specNameSort));
            //调用mapper层插入规格的方法
            specMapper.addSpec(spec);
            //因为id是自增的，所以添加完后id就有值了
            long id = spec.getId();
            //插入每个规格对应多个规格值
            String specValueInfo = specValueInfoArr[i];
            String[] specValueInfoList = specValueInfo.split(",");
            List<SpecValue> specValueList = new ArrayList<>();
            for (String s : specValueInfoList) {
                String[] tempArr = s.split("=");
                String specValue = tempArr[0];
                String specValueSort = tempArr[1];
                //创建规格值对象
                SpecValue specVal = new SpecValue();
                //给对象属性赋值
                specVal.setSpecValue(specValue);
                specVal.setSort(Integer.parseInt(specValueSort));
                specVal.setSpecId(id);
                //调用mapper层插入规格值的方法
                //specValueMapper.addSpecValue(specVal);
                specValueList.add(specVal);
            }
            //批量插入
            specValueMapper.addBatch(specValueList);
        }
        return ServerResponse.success();
    }

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    @Override
    public DataTableResult findList(SpecQueryParam specQueryParam) {
        Long count = specMapper.findListCount(specQueryParam);
        List<Spec> specList = specMapper.findPageList(specQueryParam);
        return new DataTableResult(specQueryParam.getDraw(),count,count,specList);
    }

    @Override
    public ServerResponse findSpec(Long id) {
        //获取对应的规格信息
        Spec spec = specMapper.findSpec(id);
        //根据规格id获取规格值表中对应的规格值
        List<SpecValue> specValueList = specValueMapper.findSpecValueBySpecId(id);
        //组装数据
        SpecVo specVo = new SpecVo();
        specVo.setSpec(spec);
        specVo.setSpecValueList(specValueList);
        return ServerResponse.success(specVo);
    }

    @Override
    public ServerResponse updateSpec(SpecParam specParam) {
        //获取到参数信息
        Long id = specParam.getId();
        String specNames = specParam.getSpecNames();//颜色
        String specNameSorts = specParam.getSpecNameSorts();//1
        //黑色=1，红色=2
        String specValueInfos = specParam.getSpecValueInfos();
        //进行非空验证
        if(StringUtils.isEmpty(specNames) || StringUtils.isEmpty(specNameSorts)
                || StringUtils.isEmpty(specValueInfos)){
            return ServerResponse.error(ResponseEnum.SPEC_INFO_IS_NULL);
        }
        //修改规格
        Spec spec = new Spec();
        spec.setSpecName(specNames);
        spec.setSort(Integer.parseInt(specNameSorts));
        spec.setId(id);
        specMapper.updateSpec(spec);
        //删除规格对应的规格值
        specValueMapper.deleteSpecValueBySpecId(id);
        //重新插入新的规格值
        String[] specValueInfoList = specValueInfos.split(",");
        for (String s : specValueInfoList) {
            String[] tempArr = s.split("=");
            String specValue = tempArr[0];
            String specValueSort = tempArr[1];
            //创建规格值对象
            SpecValue specVal = new SpecValue();
            //给对象属性赋值
            specVal.setSpecValue(specValue);
            specVal.setSort(Integer.parseInt(specValueSort));
            specVal.setSpecId(id);
            //调用mapper层插入规格值的方法
            specValueMapper.addSpecValue(specVal);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteSpec(Long id) {
        //删除规格信息
        specMapper.deleteSpec(id);
        //删除规格值信息
        specValueMapper.deleteSpecValueBySpecId(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.error();
        }
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String id : idArr) {
            idList.add(Long.parseLong(id));
        }
        //批量删除规格
        specMapper.deleteBatch(idList);
        //批量删除规格值
        specValueMapper.deleteBatch(idList);
        return ServerResponse.success();
    }

}
