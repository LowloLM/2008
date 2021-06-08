package com.fh.shop.spec.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.TableResult;
import com.fh.shop.spec.mapper.ISpecMapper;
import com.fh.shop.spec.mapper.ISpecValueMapper;
import com.fh.shop.spec.param.SpecAddParam;
import com.fh.shop.spec.param.SpecParam;
import com.fh.shop.spec.param.SpecQueryParam;
import com.fh.shop.spec.po.Spec;
import com.fh.shop.spec.po.SpecValue;
import com.fh.shop.spec.vo.SpecVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("specService")
public class ISpecServiceImpl implements ISpecService {

    @Autowired
    private ISpecMapper specMapper;

    @Autowired
    private ISpecValueMapper specValueMapper;

   /* @Override
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
    }*/

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    @Override
    public ServerResponse findList(SpecQueryParam specQueryParam) {
        Long count = specMapper.findListCount(specQueryParam);
        List<Spec> specList = specMapper.findPageList(specQueryParam);
        TableResult tableResult = new TableResult(count,specList);
        return ServerResponse.success(tableResult);
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
    public ServerResponse updateSpec(SpecAddParam specAddParam) {
        Spec spec = specAddParam.getSpec();
        Long specId = spec.getId();
//        更新spec
        specMapper.updateById(spec);
        //先删除specValues
        QueryWrapper<SpecValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("specId",specId);
        specValueMapper.delete(queryWrapper);
        List<SpecValue> specValueList = specAddParam.getSpecValueList();
        List<SpecValue> specValues = specValueList.stream().map(y -> {
            SpecValue specValue = new SpecValue();
            specValue.setSort(y.getSort());
            specValue.setSpecId(specId);
            specValue.setSpecValue(y.getSpecValue());
            return specValue;
        }).collect(Collectors.toList());
        specValueMapper.addBatch(specValues);
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

    @Override
    public ServerResponse addSpec(List<SpecAddParam> specParam) {
        specParam.stream().forEach(x ->{
            //插入规格
            Spec spec = x.getSpec();
            specMapper.insert(spec);
            long specId = spec.getId();
            //插入规格值表
            List<SpecValue> specValueList = x.getSpecValueList();
            List<SpecValue> specValues = specValueList.stream().map(y -> {
                SpecValue specValue = new SpecValue();
                specValue.setSort(y.getSort());
                specValue.setSpecId(specId);
                specValue.setSpecValue(y.getSpecValue());
                return specValue;
            }).collect(Collectors.toList());
            specValueMapper.addBatch(specValues);
        });
        return ServerResponse.success();
    }

}
