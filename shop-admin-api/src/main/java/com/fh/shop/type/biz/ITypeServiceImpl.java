package com.fh.shop.type.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.brand.mapper.IBrandMapper;
import com.fh.shop.brand.po.Brand;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.spec.mapper.ISpecMapper;
import com.fh.shop.spec.po.Spec;
import com.fh.shop.type.mapper.*;
import com.fh.shop.type.param.AttrParam;
import com.fh.shop.type.param.TypeParam;
import com.fh.shop.type.po.*;
import com.fh.shop.type.vo.TypeInfoVo;
import com.fh.shop.type.vo.TypeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("typeService")
@Transactional(rollbackFor = Exception.class)
public class ITypeServiceImpl implements ITypeService {

    @Autowired
    private IBrandMapper brandMapper;
    @Autowired
    private ISpecMapper specMapper;
    @Autowired
    private ITypeMapper typeMapper;
    @Autowired
    private ITypeBrandMapper typeBrandMapper;
    @Autowired
    private ITypeSpecMapper typeSpecMapper;
    @Autowired
    private IAttrMapper attrMapper;
    @Autowired
    private IAttrValueMapper attrValueMapper;

    @Transactional(readOnly = true)
    @Override
    public ServerResponse findInfo() {
        //获取所有的品牌
        List<Brand> brandList = brandMapper.selectList(null);
        //获取所有的规格
        List<Spec> specList = specMapper.selectList(null);
        //组装
        TypeVo typeVo = new TypeVo();
        typeVo.setBrandList(brandList);
        typeVo.setSpecList(specList);
        return ServerResponse.success(typeVo);
    }

    @Override
    public ServerResponse addType(TypeParam typeParam) {
        //类型
        String typeName = typeParam.getTypeName();
        Type type = new Type();
        type.setTypeName(typeName);
        typeMapper.insert(type);
        //商品类型
        Long id = type.getId();
        List<Long> checkedBrandList = typeParam.getCheckedBrandList();
        if (checkedBrandList.size()>0){
            List<TypeBrand> typeBrandList = checkedBrandList.stream().map(x -> {
                TypeBrand typeBrand = new TypeBrand();
                typeBrand.setBrandId(x);
                typeBrand.setTypeId(id);
                return typeBrand;
            }).collect(Collectors.toList());

            typeBrandMapper.addBatch(typeBrandList);
        }
        //类型规格
        List<Long> checkedSpecList = typeParam.getCheckedSpecList();
        if (checkedSpecList.size()>0){
            List<TypeSpec> collect = checkedSpecList.stream().map(x -> {
                TypeSpec typeSpec = new TypeSpec();
                typeSpec.setSpecId(x);
                typeSpec.setTypeId(id);
                return typeSpec;
            }).collect(Collectors.toList());
            typeSpecMapper.addBatch(collect);
        }
        //属性
        List<AttrParam> attrParamList = typeParam.getAttrParamList();
        if (attrParamList.size()>0){
            attrParamList.forEach(x ->{
                Attr attr = new Attr();
                attr.setAttrName(x.getAttrName());
                attr.setTypeId(id);
                attrMapper.insert(attr);
                Long attrId = attr.getId();
                String attrValues = x.getAttrValues();
                if (StringUtils.isEmpty(attrValues)){
                    List<AttrValue> collect = Arrays.stream(attrValues.split(",")).map(y -> {
                        AttrValue attrValue = new AttrValue();
                        attrValue.setAttrId(attrId);
                        attrValue.setAttrValue(y);
                        return attrValue;
                    }).collect(Collectors.toList());
                    attrValueMapper.addBatch(collect);
                }
            });

        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findType(Long id) {
        //获取品牌表
        List<Brand> brands = brandMapper.selectList(null);
        //获取规格表
        List<Spec> specs = specMapper.selectList(null);
        //过去类型
        Type type = typeMapper.selectById(id);
        //获取选中信息表
        QueryWrapper<TypeBrand> typeBrandQueryWrapper = new QueryWrapper<>();
        typeBrandQueryWrapper.eq("typeid",id);
        List<TypeBrand> typeBrandList = typeBrandMapper.selectList(typeBrandQueryWrapper);
        //获取选中规格
        QueryWrapper<TypeSpec> typeSpecQueryWrapper = new QueryWrapper<>();
        typeSpecQueryWrapper.eq("typeid",id);
        List<TypeSpec> typeSpecList = typeSpecMapper.selectList(typeSpecQueryWrapper);
        //获取对应的属性和属性值
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("typeid",id);
        List<Attr> attrs = attrMapper.selectList(attrQueryWrapper);
        List<Long> attrIdList = attrs.stream().map(x -> x.getId()).collect(Collectors.toList());
        //属性值
        QueryWrapper<AttrValue>attrValueQueryWrapper  = new QueryWrapper<>();
        attrValueQueryWrapper.in("attrid",attrIdList);
        List<AttrValue> attrValues = attrValueMapper.selectList(attrValueQueryWrapper);
        List<AttrParam> attrParams = new ArrayList<>();
        attrs.forEach(x ->{
            Long attrId = x.getId();
            List<AttrValue> attrValueList = attrValues.stream().filter(y -> y.getAttrId().longValue() == attrId.longValue()).collect(Collectors.toList());
            AttrParam attrParam = new AttrParam();
            attrParam.setAttrName(x.getAttrName());
            attrParam.setAttrValues(StringUtils.join(attrValueList.stream().map(z -> z.getAttrValue()).collect(Collectors.toList()), ","));
            attrParams.add(attrParam);
        });
        TypeInfoVo typeInfoVo = new TypeInfoVo();
        typeInfoVo.setId(id);
        typeInfoVo.setTypeName(type.getTypeName());
        typeInfoVo.setBrandList(brands);
        typeInfoVo.setSpecList(specs);
        typeInfoVo.setCheckedBrandList(typeBrandList.stream().map(x-> x.getBrandId()).collect(Collectors.toList()));
        typeInfoVo.setCheckedSpecList(typeSpecList.stream().map(x-> x.getSpecId()).collect(Collectors.toList()));
        typeInfoVo.setAttrParamList(attrParams);
        return ServerResponse.success(typeInfoVo);
    }

}
