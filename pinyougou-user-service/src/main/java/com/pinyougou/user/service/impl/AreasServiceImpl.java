package com.pinyougou.user.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbAreas;
import com.pinyougou.entity.TbAreasExample;
import com.pinyougou.entity.TbAreasExample.Criteria;
import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.user.service.AreasService;

import domain.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class AreasServiceImpl implements AreasService {

	@Autowired
	private TbAreasMapper areasMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbAreas> findAll() {
		return areasMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbAreas> page=   (Page<TbAreas>) areasMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbAreas areas) {
		areasMapper.insert(areas);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbAreas areas){
		areasMapper.updateByPrimaryKey(areas);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbAreas findOne(Long id){
		return areasMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			areasMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbAreas areas, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbAreasExample example=new TbAreasExample();
		Criteria criteria = example.createCriteria();
		
		if(areas!=null){			
						if(areas.getAreaid()!=null && areas.getAreaid().length()>0){
				criteria.andAreaidLike("%"+areas.getAreaid()+"%");
			}
			if(areas.getArea()!=null && areas.getArea().length()>0){
				criteria.andAreaLike("%"+areas.getArea()+"%");
			}
			if(areas.getCityid()!=null && areas.getCityid().length()>0){
				criteria.andCityidLike("%"+areas.getCityid()+"%");
			}
	
		}
		
		Page<TbAreas> page= (Page<TbAreas>)areasMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 根据cityId查区域列表
	 * @param cityId
	 * @return
	 */
	@Override
	public List<TbAreas> findByCityId(String cityId) {
		TbAreasExample example = new TbAreasExample();
		Criteria criteria = example.createCriteria();
		criteria.andCityidEqualTo(cityId);
		return areasMapper.selectByExample(example);
	}

}
