package com.pinyougou.user.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.entity.TbAddress;
import com.pinyougou.entity.TbAddressExample;
import com.pinyougou.entity.TbAddressExample.Criteria;
import com.pinyougou.entity.TbAreasExample;
import com.pinyougou.entity.TbCitiesExample;
import com.pinyougou.entity.TbProvincesExample;
import com.pinyougou.mapper.TbAddressMapper;
import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.user.service.AddressService;

import domain.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private TbAddressMapper addressMapper;
	@Autowired
	private TbProvincesMapper provincesMapper;
	@Autowired
	private TbCitiesMapper citiesMapper;
	@Autowired
	private TbAreasMapper areasMapper;
	
	
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbAddress> findAll(String userId) {
		TbAddressExample example = new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<TbAddress> addressList = addressMapper.selectByExample(example);
		//将省市区的Str写入address中
		for (TbAddress address : addressList) {
			//省
			TbProvincesExample example1 = new TbProvincesExample();
			TbProvincesExample.Criteria criteria1 = example1.createCriteria();
			criteria1.andProvinceidEqualTo(address.getProvinceId());
			address.setProvinceStr(provincesMapper.selectByExample(example1).get(0).getProvince());
			//市
			TbCitiesExample example2 = new TbCitiesExample();
			TbCitiesExample.Criteria criteria2 = example2.createCriteria();
			criteria2.andCityidEqualTo(address.getCityId());
			address.setCityStr(citiesMapper.selectByExample(example2).get(0).getCity());
			//区
			TbAreasExample example3 = new TbAreasExample();
			TbAreasExample.Criteria criteria3 = example3.createCriteria();
			criteria3.andAreaidEqualTo(address.getTownId());
			address.setTownStr(areasMapper.selectByExample(example3).get(0).getArea());
			//手机号加****
			String mobile = address.getMobile();
			address.setMobile(mobile.substring(0,3)+"****"+ mobile.substring(7, mobile.length()));
		}
		return addressList;	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbAddress> page=   (Page<TbAddress>) addressMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbAddress address) {
		addressMapper.insert(address);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbAddress address){
		addressMapper.updateByPrimaryKey(address);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbAddress findOne(Long id){
		return addressMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			addressMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbAddress address, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbAddressExample example=new TbAddressExample();
		Criteria criteria = example.createCriteria();
		
		if(address!=null){			
						if(address.getUserId()!=null && address.getUserId().length()>0){
				criteria.andUserIdLike("%"+address.getUserId()+"%");
			}
			if(address.getProvinceId()!=null && address.getProvinceId().length()>0){
				criteria.andProvinceIdLike("%"+address.getProvinceId()+"%");
			}
			if(address.getCityId()!=null && address.getCityId().length()>0){
				criteria.andCityIdLike("%"+address.getCityId()+"%");
			}
			if(address.getTownId()!=null && address.getTownId().length()>0){
				criteria.andTownIdLike("%"+address.getTownId()+"%");
			}
			if(address.getMobile()!=null && address.getMobile().length()>0){
				criteria.andMobileLike("%"+address.getMobile()+"%");
			}
			if(address.getAddress()!=null && address.getAddress().length()>0){
				criteria.andAddressLike("%"+address.getAddress()+"%");
			}
			if(address.getContact()!=null && address.getContact().length()>0){
				criteria.andContactLike("%"+address.getContact()+"%");
			}
			if(address.getIsDefault()!=null && address.getIsDefault().length()>0){
				criteria.andIsDefaultLike("%"+address.getIsDefault()+"%");
			}
			if(address.getNotes()!=null && address.getNotes().length()>0){
				criteria.andNotesLike("%"+address.getNotes()+"%");
			}
			if(address.getAlias()!=null && address.getAlias().length()>0){
				criteria.andAliasLike("%"+address.getAlias()+"%");
			}
	
		}
		
		Page<TbAddress> page= (Page<TbAddress>)addressMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<TbAddress> findListByUserId(String userId) {
		
		TbAddressExample example=new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		return addressMapper.selectByExample(example);
	}
	/**
	 * 设置默认地址
	 * @param addressId
	 */
	@Override
	public void updateDefault(Long addressId,String userId) {
		TbAddressExample example = new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<TbAddress> addresses = addressMapper.selectByExample(example);
		for (TbAddress address : addresses) {
			if (address.getId() == addressId) {
				address.setIsDefault("1");
				addressMapper.updateByPrimaryKey(address);
			} else {
				address.setIsDefault("0");
				addressMapper.updateByPrimaryKey(address);
			}
		}

	}
	
}
