package com.pinyougou.mapper;

import com.pinyougou.entity.TbFreightTemplate;
import com.pinyougou.entity.TbFreightTemplateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbFreightTemplateMapper {
    int countByExample(TbFreightTemplateExample example);

    int deleteByExample(TbFreightTemplateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbFreightTemplate record);

    int insertSelective(TbFreightTemplate record);

    List<TbFreightTemplate> selectByExample(TbFreightTemplateExample example);

    TbFreightTemplate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbFreightTemplate record, @Param("example") TbFreightTemplateExample example);

    int updateByExample(@Param("record") TbFreightTemplate record, @Param("example") TbFreightTemplateExample example);

    int updateByPrimaryKeySelective(TbFreightTemplate record);

    int updateByPrimaryKey(TbFreightTemplate record);
}