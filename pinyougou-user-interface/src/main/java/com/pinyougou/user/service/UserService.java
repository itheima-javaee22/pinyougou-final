package com.pinyougou.user.service;
import java.util.List;

import com.pinyougou.entity.TbUser;

import domain.PageResult;



/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user,String userName);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbUser user, int pageNum,int pageSize);

	/**
	 * 生成短信验证码
	 * @param phone
	 */
	public void creatSmsCode(String phone);
	/**
	 * 判断短信验证码是否正确
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean checkSmsCode(String phone,String code);
	
	/**
	 * 根据用户名查询
	 * @param username
	 * @return
	 */
	public TbUser findByUsername(String username);
}
