package com.pinyougou.user.service;
import java.util.List;

import com.pinyougou.entity.TbAreas;

import domain.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface AreasService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAreas> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbAreas areas);
	
	
	/**
	 * 修改
	 */
	public void update(TbAreas areas);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAreas findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbAreas areas, int pageNum, int pageSize);

	/**
	 * 根据cityId查区域列表
	 * @param cityId
	 * @return
	 */
	public List<TbAreas> findByCityId(String cityId);
}
