package com.pinyougou.user.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.TbProvinces;
import com.pinyougou.user.service.ProvincesService;

import domain.PageResult;
import domain.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/provinces")
public class ProvincesController {

	@Reference
	private ProvincesService provincesService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbProvinces> findAll(){			
		return provincesService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return provincesService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param provinces
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbProvinces provinces){
		try {
			provincesService.add(provinces);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param provinces
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbProvinces provinces){
		try {
			provincesService.update(provinces);
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
	public TbProvinces findOne(Long id){
		return provincesService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			provincesService.delete(ids);
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
	public PageResult search(@RequestBody TbProvinces provinces, int page, int rows  ){
		return provincesService.findPage(provinces, page, rows);		
	}
	
}
