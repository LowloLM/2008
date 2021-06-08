package com.fh.shop.spec.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.spec.param.SpecQueryParam;
import com.fh.shop.spec.po.Spec;

import java.util.List;

public interface ISpecMapper  extends BaseMapper<Spec> {

    /***
     * 新增
     * @param spec
     */
    //void addSpec(Spec spec);

    /***
     * 查询总条数
     * @param specQueryParam
     * @return
     */
    Long findListCount(SpecQueryParam specQueryParam);

    /***
     * 查询分页列表
     * @param specQueryParam
     * @return
     */
    List<Spec> findPageList(SpecQueryParam specQueryParam);

    Spec findSpec(Long id);

    void updateSpec(Spec spec);

    void deleteSpec(Long id);

    void deleteBatch(List<Long> idList);

    List<Spec> findAllSpecList();

    List<Spec> findListByTypeId(Long typeId);
}
