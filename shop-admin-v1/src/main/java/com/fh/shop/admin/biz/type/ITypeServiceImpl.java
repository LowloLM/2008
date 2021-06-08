package com.fh.shop.admin.biz.type;

import com.fh.shop.admin.mapper.brand.IBrandMapper;
import com.fh.shop.admin.mapper.spec.ISpecMapper;
import com.fh.shop.admin.mapper.type.*;
import com.fh.shop.admin.param.TypeParam;
import com.fh.shop.admin.param.TypeQueryParam;
import com.fh.shop.admin.po.brand.Brand;
import com.fh.shop.admin.po.spec.Spec;
import com.fh.shop.admin.po.type.*;
import com.fh.shop.admin.vo.type.AttrVo;
import com.fh.shop.admin.vo.type.TypeEditVo;
import com.fh.shop.admin.vo.type.TypeVo;
import com.fh.shop.common.DataTableResult;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("typeService")
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

    @Override
    public ServerResponse findInfo() {
        //获取所有的品牌
        List<Brand> brandList = brandMapper.findAllBrandList();
        //获取所有的规格
        List<Spec> specList = specMapper.findAllSpecList();
        //组装
        TypeVo typeVo = new TypeVo();
        typeVo.setBrandList(brandList);
        typeVo.setSpecList(specList);
        return ServerResponse.success(typeVo);
    }

    /***
     * 新增
     * @param typeParam
     * @return
     */
    @Override
    public ServerResponse addType(TypeParam typeParam) {
        //获取到参数信息
        String typeName = typeParam.getTypeName();
        String brandLists = typeParam.getBrandList();
        String typeLists = typeParam.getTypeList();
        String attrNames = typeParam.getAttrNames();
        String attrValues = typeParam.getAttrValues();
        //进行非空验证
        if(StringUtils.isEmpty(typeName) || StringUtils.isEmpty(brandLists)
                                         || StringUtils.isEmpty(typeLists)
                                         || StringUtils.isEmpty(attrNames)
                                         || StringUtils.isEmpty(attrValues)){
            return ServerResponse.error(ResponseEnum.TYPE_INFO_IS_NULL);
        }
        Type type = new Type();
        type.setTypeName(typeName);
        typeMapper.addType(type);
        Long id = type.getId();
        //对参数信息进行拆分
        String[] brandArr = brandLists.split(",");
        String[] typeArr = typeLists.split(",");
        List<TypeBrand> typeBrandList = new ArrayList<>();
        for (int i = 0; i < brandArr.length; i++) {
            String brandIds = brandArr[i];
            TypeBrand typeBrand = new TypeBrand();
            typeBrand.setBrandId(Long.parseLong(brandIds));
            typeBrand.setTypeId(id);
            typeBrandList.add(typeBrand);
        }
        typeBrandMapper.addBatch(typeBrandList);
        List<TypeSpec> typeSpecList = new ArrayList<>();
        for (int i = 0; i < typeArr.length; i++) {
            String typeIds = typeArr[i];
            TypeSpec typeSpec = new TypeSpec();
            typeSpec.setSpecId(Long.parseLong(typeIds));
            typeSpec.setTypeId(id);
            typeSpecList.add(typeSpec);
        }
        typeSpecMapper.addBatch(typeSpecList);
        //插入属性表
        String[] attrNameArr = attrNames.split(",");
        String[] attrValueArr = attrValues.split(";");
        for (int i = 0; i < attrNameArr.length; i++) {
            String attrName = attrNameArr[i];
            Attr attr = new Attr();
            attr.setAttrName(attrName);
            attr.setTypeId(id);
            attrMapper.addAttr(attr);
            Long attrId = attr.getId();
            List<AttrValue> attrValueList = new ArrayList<>();
            String[] attrValueStrArr = attrValueArr[i].split(",");
            for (String attrValueStr : attrValueStrArr) {
                AttrValue attrValue = new AttrValue();
                attrValue.setAttrId(attrId);
                attrValue.setAttrValue(attrValueStr);
                attrValueList.add(attrValue);
            }
            //批量插入属性值表
            attrValueMapper.addBatch(attrValueList);
        }
        return ServerResponse.success();
    }

    @Override
    public DataTableResult findList(TypeQueryParam typeQueryParam) {
        Long count = typeMapper.findListCount(typeQueryParam);
        List<Type> typeList = typeMapper.findPageList(typeQueryParam);
        return new DataTableResult(typeQueryParam.getDraw(),count,count,typeList);
    }

    @Override
    public ServerResponse findType(Long id) {
        //根据类型id获取对应的类型信息
        Type type = typeMapper.findType(id);
        //根据类型id获取对应的类型品牌的信息
        List<Long> brandIdList = typeBrandMapper.findTypeBrandIdList(id);
        //根据类型id获取对应的类型规格的信息
        List<Long> specIdList = typeSpecMapper.findTypeSpecIdList(id);
        //获取所有的品牌列表
        List<Brand> allBrandList = brandMapper.findAllBrandList();
        //获取所有的规格列表
        List<Spec> allSpecList = specMapper.findAllSpecList();
        //获取类型对应的属性名
        List<Attr> attrList = attrMapper.findAttrListByTypeId(id);
        List<Long> attrIdList = new ArrayList<>();
        List<AttrVo> attrVoList = new ArrayList<>();
        for (Attr attr : attrList) {
            attrIdList.add(attr.getId());
        }
        //获取属性对应的属性值列表
        List<AttrValue> attrValueList = attrValueMapper.findAttrValueListByAttrIdList(attrIdList);
        //如何解决属性和属性值的对应关系
        for (Attr attr : attrList) {
            AttrVo attrVo = new AttrVo();
            attrVo.setAttrName(attr.getAttrName());
            attrVo.setAttrValues(buildAttrValues(attr.getId(),attrValueList));
            attrVoList.add(attrVo);
        }
        TypeEditVo typeEditVo = new TypeEditVo();
        typeEditVo.setBrandIdList(brandIdList);
        typeEditVo.setBrandList(allBrandList);
        typeEditVo.setType(type);
        typeEditVo.setSpecIdList(specIdList);
        typeEditVo.setSpecList(allSpecList);
        typeEditVo.setAttrVoList(attrVoList);
        return ServerResponse.success(typeEditVo);
    }

    private String buildAttrValues(Long attrId,List<AttrValue> attrValueList){
        String result = "";
        List<String> attrValues = new ArrayList<>();
        for (AttrValue attrValue : attrValueList) {
            if(attrValue.getAttrId().longValue() == attrId.longValue()){
                attrValues.add(attrValue.getAttrValue());
            }
        }
        result = StringUtils.join(attrValues,",");
        return result;
    }

    /***
     * 修改
     * @param typeParam
     * @return
     */
    @Override
    public ServerResponse updateType(TypeParam typeParam) {
        String typeName = typeParam.getTypeName();
        String typeList = typeParam.getTypeList();
        String brandList = typeParam.getBrandList();
        String attrNames = typeParam.getAttrNames();
        String attrValues = typeParam.getAttrValues();
        Long id = typeParam.getId();
        Type type = new Type();
        type.setTypeName(typeName);
        type.setId(id);
        // 更新类型信息
        typeMapper.updateType(type);
        // 根据类型id删除与其对应的品牌关联信息
        typeBrandMapper.deleteTypeBrandByTypeId(id);
        // 重新插入新的品牌关联信息
        List<TypeBrand> typeBrandList = new ArrayList<>();
        String[] brandArr = brandList.split(",");
        for (int i = 0; i < brandArr.length; i++) {
            String brandIds = brandArr[i];
            TypeBrand typeBrand = new TypeBrand();
            typeBrand.setBrandId(Long.parseLong(brandIds));
            typeBrand.setTypeId(id);
            typeBrandList.add(typeBrand);
        }
        typeBrandMapper.addBatch(typeBrandList);
        // 根据类型id删除与其对应的规格关联信息
        typeSpecMapper.deleteTypeSpecByTypeId(id);
        // 重新插入新的规格关联信息
        List<TypeSpec> typeSpecList = new ArrayList<>();
        String[] typeArr = typeList.split(",");
        for (int i = 0; i < typeArr.length; i++) {
            String typeIds = typeArr[i];
            TypeSpec typeSpec = new TypeSpec();
            typeSpec.setSpecId(Long.parseLong(typeIds));
            typeSpec.setTypeId(id);
            typeSpecList.add(typeSpec);
        }
        typeSpecMapper.addBatch(typeSpecList);
        //查询类型对应的属性id的集合
        List<Attr> attrList = attrMapper.findAttrListByTypeId(id);
        List<Long> attrIdList = new ArrayList<>();
        for (Attr attr : attrList) {
            attrIdList.add(attr.getId());
        }
        //删除属性
        attrMapper.deleteByTypeId(id);
        //删除属性对应的属性值
        attrValueMapper.deleteByAttrIdList(attrIdList);
        //插入属性
        String[] attrNameArr = attrNames.split(",");
        String[] attrValueArr = attrValues.split(";");
        int count = 0;
        for (String attrName : attrNameArr) {
            Attr attr = new Attr();
            attr.setTypeId(id);
            attr.setAttrName(attrName);
            attrMapper.addAttr(attr);
            Long attrId = attr.getId();
            //批量插入属性值
            List<AttrValue> attrValueList = new ArrayList<>();
            String[] attrValArr = attrValueArr[count++].split(",");
            for (String attrVal : attrValArr) {
                AttrValue attrValue = new AttrValue();
                attrValue.setAttrValue(attrVal);
                attrValue.setAttrId(attrId);
                attrValueList.add(attrValue);
            }
            attrValueMapper.addBatch(attrValueList);
        }
        return ServerResponse.success();
    }

    /***
     * 删除
     * @param id
     * @return
     */
    @Override
    public ServerResponse deleteType(Long id) {
        //删除类型信息
        typeMapper.deleteType(id);
        //删除品牌类型关联表信息
        typeBrandMapper.deleteTypeBrandByTypeId(id);
        //删除规格类型关联表信息
        typeSpecMapper.deleteTypeSpecByTypeId(id);
        //先获取属性表的id
        List<Long> attrList = attrMapper.findAttr(id);
        //删除属性表
        attrMapper.deleteAttr(id);
        //删除属性值表
        attrValueMapper.deleteAttrValue(attrList);
        return ServerResponse.success();
    }

    /***
     * 批量删除
     * @param ids
     * @return
     */
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
        List<Attr> attrList = attrMapper.findAttrValue(idList);
        List<Long> attrIdList = new ArrayList<>();
        for (Attr attr : attrList) {
            attrIdList.add(attr.getId());
        }
        //批量删除类型信息
        typeMapper.deleteBatch(idList);
        // 批量删除品牌类型关联表信息
        typeBrandMapper.deleteBatch(idList);
        //批量删除规格类型关联表信息
        typeSpecMapper.deleteBatch(idList);
        //批量删除属性信息
        attrMapper.deleteBatch(idList);
        //批量删除属性值信息
        attrValueMapper.deleteByAttrIdList(attrIdList);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse findAllType() {
        List<Type> typeList = typeMapper.findAllType();
        return ServerResponse.success(typeList);
    }
}
