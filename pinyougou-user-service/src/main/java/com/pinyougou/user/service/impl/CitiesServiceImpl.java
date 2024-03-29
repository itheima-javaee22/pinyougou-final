package com.pinyougou.user.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbCities;
import com.pinyougou.entity.TbCitiesExample;
import com.pinyougou.entity.TbCitiesExample.Criteria;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.user.service.CitiesService;

import domain.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class CitiesServiceImpl implements CitiesService {

	@Autowired
	private TbCitiesMapper citiesMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbCities> findAll() {
		return citiesMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbCities> page=   (Page<TbCities>) citiesMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbCities cities) {
		citiesMapper.insert(cities);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbCities cities){
		citiesMapper.updateByPrimaryKey(cities);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbCities findOne(Long id){
		return citiesMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			citiesMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbCities cities, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbCitiesExample example=new TbCitiesExample();
		Criteria criteria = example.createCriteria();
		
		if(cities!=null){			
						if(cities.getCityid()!=null && cities.getCityid().length()>0){
				criteria.andCityidLike("%"+cities.getCityid()+"%");
			}
			if(cities.getCity()!=null && cities.getCity().length()>0){
				criteria.andCityLike("%"+cities.getCity()+"%");
			}
			if(cities.getProvinceid()!=null && cities.getProvinceid().length()>0){
				criteria.andProvinceidLike("%"+cities.getProvinceid()+"%");
			}
	
		}
		
		Page<TbCities> page= (Page<TbCities>)citiesMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 根据provinceId查询城市列表
	 * @param provinceId
	 * @return
	 */
	@Override
	public List<TbCities> findByProvinceId(String provinceId) {
		TbCitiesExample example = new TbCitiesExample();
		Criteria criteria = example.createCriteria();
		criteria.andProvinceidEqualTo(provinceId);
		return citiesMapper.selectByExample(example);
	}

}
