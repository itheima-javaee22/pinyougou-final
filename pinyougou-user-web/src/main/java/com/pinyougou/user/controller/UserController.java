package com.pinyougou.user.controller;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.TbUser;
import com.pinyougou.user.service.UserService;

import domain.PageResult;
import domain.Result;
import util.PhoneFormatCheckUtils;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smscode){
		
		boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smscode);
		if(checkSmsCode==false) {
			return new Result(false, "验证码输入错误！");
		}
		try {
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		//获取用户名
		String name = SecurityContextHolder.getContext( ).getAuthentication( ).getName( );

		try {
			userService.update(user,name);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}
	
	/**
	 * 发送短信验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping("/sendCode")
	public Result sendCode(String phone) {
		//判断手机号格式
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
			return new Result(false, "手机号格式不正确");
		}
		try {
			userService.creatSmsCode(phone);
			return new Result(true, "验证码发送成功");
		}catch (Exception e) {
			e.printStackTrace();
			return new Result(true, "验证码发送失败");
		}
	}

	/**
	 * 获取登录名
	 * @return
	 */
	@RequestMapping("findByUsername")
	public TbUser findByUsername(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findByUsername(username);
	}
	
	/**
	 * 验证手机短信
	 * @param phone
	 * @param code
	 * @return
	 */
	@RequestMapping("/checkSmsCode")
	public Result checkSmsCode( String phone, String code) {
		//校验验证码是否正确
		boolean checkSmsCode = userService.checkSmsCode(phone, code);
		if(!checkSmsCode){
			return new Result(false, "验证码不正确！");
		}
		return new Result(true, "验证码正确！");
	}
	
}
