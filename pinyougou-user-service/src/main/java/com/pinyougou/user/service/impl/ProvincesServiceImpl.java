package com.pinyougou.user.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbProvinces;
import com.pinyougou.entity.TbProvincesExample;
import com.pinyougou.entity.TbProvincesExample.Criteria;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.user.service.ProvincesService;

import domain.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ProvincesServiceImpl implements ProvincesService {

	@Autowired
	private TbProvincesMapper provincesMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbProvinces> findAll() {
		return provincesMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbProvinces> page=   (Page<TbProvinces>) provincesMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbProvinces provinces) {
		provincesMapper.insert(provinces);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbProvinces provinces){
		provincesMapper.updateByPrimaryKey(provinces);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbProvinces findOne(Long id){
		return provincesMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			provincesMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbProvinces provinces, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbProvincesExample example=new TbProvincesExample();
		Criteria criteria = example.createCriteria();
		
		if(provinces!=null){			
						if(provinces.getProvinceid()!=null && provinces.getProvinceid().length()>0){
				criteria.andProvinceidLike("%"+provinces.getProvinceid()+"%");
			}
			if(provinces.getProvince()!=null && provinces.getProvince().length()>0){
				criteria.andProvinceLike("%"+provinces.getProvince()+"%");
			}
	
		}
		
		Page<TbProvinces> page= (Page<TbProvinces>)provincesMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
